package mining;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import util.Configuration;
import org.tartarus.snowball.ext.PorterStemmer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import data.Words;
import database.DbConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TopicAnalysis {
	
	private int numDocs = 0;
	
	private SentenceDetector sentDetector = null;
	private POSTagger posTagger = null;
	private Tokenizer tokenizer = null;
	
	public String[] getSentences ( String paragraph ) { return sentDetector.sentDetect( paragraph ); }
	
	public String[] getTokens ( String sentence ) { return tokenizer.tokenize( sentence ); }
	
	public String[] getPOS ( String[] tokens ) { return posTagger.tag( tokens ); }
	
	public TopicAnalysis ( Configuration config, int num ) 
	{ 
		numDocs = num; 
		try {
			
		    sentDetector = new SentenceDetectorME( config.getSentenceModel() );
		    posTagger = new POSTaggerME( config.getPosModel() );
		    tokenizer = new TokenizerME( config.getTokenizerModel() );
			   
		} catch (Exception e) { System.out.println(e.toString()); }	
	}
	
	public String stemTerm (String word) {
		
	    PorterStemmer stemmer = new PorterStemmer();
	    stemmer.setCurrent(word);
	    stemmer.stem();
	    return stemmer.getCurrent();
	    
	}
	
	public ArrayList <String> stemTerms (ArrayList <String> words) {
		
		ArrayList <String> stemmed = new ArrayList <String> ();
		for ( int i=0; i<words.size(); i++ ) 
		{
			PorterStemmer stemmer = new PorterStemmer();
			stemmer.setCurrent(words.get(i));
			stemmer.stem();
			stemmed.add( stemmer.getCurrent() );
		}
		return stemmed;
	}
	
	private boolean isNoun ( String pos ) {
	
		// NN Noun, singular or mass
		// NNS Noun, plural
		// NNP Proper noun, singular
		// NNPS Proper noun, plural
		pos = pos.trim();
		if ( pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS") ) {
			return true;
		} else { return false; }
		
	}
	
	public ArrayList <String> getNouns ( String[] tokens, String[] pos ) {
	
		ArrayList <String> nouns = new ArrayList <String> ();
		for ( int i=0; i<tokens.length; i++ ) {
		   if ( isNoun(pos[i]) ) { nouns.add(tokens[i]); }
		}
		return nouns;
		
	}
	
	public ArrayList <String> parseSentences ( String[] sentences )
	{
		ArrayList <String> terms = new ArrayList <String> ();
		for ( int i=0; i<sentences.length; i++ )
		{
			String[] tokens = getTokens( sentences[i] ); 	           // break the sentence into tokens
			String[] pos = getPOS( tokens );			 	           // compute Parts of Speech
			ArrayList <String> nouns = getNouns( tokens, pos );	       // find the nouns
			ArrayList <String> stemmedNouns = stemTerms( nouns );      // stem the nouns 
			for ( int j=0; j<stemmedNouns.size(); j++ ) {			   // add unique nouns to master list
			   if ( !terms.contains(stemmedNouns.get(j)) ) { terms.add(stemmedNouns.get(j)); }
			}
		}
		return terms;
	}
	
	// given the set of nouns from the text find all co-occurring word pairs
    public HashMap <String,String> createPairs ( ArrayList <String> nouns ) {
			
    	HashMap <String,String> pairs = new HashMap <String,String> ();
		for ( int i=0; i<nouns.size(); i++ ) {
		   for ( int j=i+1; j<nouns.size(); j++ ) { // start at i+1 so we don't get the word and itself
			  pairs.put( nouns.get(i), nouns.get(j) ); 
		   }
		}
		return pairs;
			
	}
    
    public String updateDatabase ( Configuration config, HashMap <String,String> pairs )
    {
    	DbConnection database = new DbConnection ();
    	boolean connected = database.createConnection(
    			config.getDatabaseURL(), config.getDatabaseUsername(), config.getDatabasePassword());
    	if ( connected ) {
    	  
    	  Connection connection = database.getConnection();

    	  // loop over all the word pairs
    	  String sql;
    	  int pairId;
    	  int occurrence = 0;
    	  Iterator <Map.Entry<String,String>> iterator = pairs.entrySet().iterator();
    	  while ( iterator.hasNext() ) {
    	     Map.Entry <String,String> keyValue = (Map.Entry <String,String>) iterator.next();
    	        
     	     // find if this word pair already exists in the database
    	     pairId = -1;
    	     sql = "SELECT id FROM " + config.getDbCooccurrenceTable() + " WHERE word1='" + keyValue.getKey() + 
    	    		 "' AND word2='" + keyValue.getValue() + "' ";
    	     try {
    	    	    Statement stmt = connection.createStatement();
    	    	    ResultSet rs = stmt.executeQuery(sql);
    	    	    while ( rs.next() ) { pairId = rs.getInt("id"); occurrence = rs.getInt("occurrence"); }
    	    	    rs.close();
    	    	    stmt.close();
    	    } catch (Exception e) { System.out.println("Database Exception: " + e); } 
    	    
     	    // if yes, then update the occurrence field
    	    if ( pairId != -1 )
    	    {
        	   occurrence++;
    	       sql = "UPDATE " + config.getDbCooccurrenceTable() + " SET occurrence=" + 
        	      occurrence + " WHERE id=" + pairId;
    	       try {
   	    	     Statement stmt = connection.createStatement();
  	    	     stmt.executeUpdate(sql);
  	    	     stmt.close();
    	       } catch (Exception e) { System.out.println("Database Exception (updateDatabse): " + e); }
    	    } else { // if no, then add this pair and set occurrence to 1
    	       sql = "INSERT INTO " + config.getDbCooccurrenceTable() + " (word1, word2, occurrence) " +
    	                "VALUES ('" + keyValue.getKey() + "','" + keyValue.getValue() + "',1)";
     	       try {
    	    	 Statement stmt = connection.createStatement();
   	    	     stmt.executeUpdate(sql);
   	    	     stmt.close();
    	       } catch (Exception e) { System.out.println("Database Exception (updateDatabse): " + e); }
    	    }
    	     
    	  } // end while loop over all word pairs

    	} // end if connected to database
    	return database.getErrorMessage();
    }
    
    public ArrayList <Words> computeSupport ( Configuration config )
    {
	      
    	ArrayList <Words> wordList = new ArrayList <Words> ();
    	DbConnection database = new DbConnection ();
    	boolean connected = database.createConnection(
    			config.getDatabaseURL(), config.getDatabaseUsername(), config.getDatabasePassword());
    	if ( connected ) {
    	  
    	  Connection connection = database.getConnection();
    	  
    	  // get all the rows of the cooccurrence table
    	  ArrayList <Integer> occurrence = new ArrayList <Integer> ();
    	  ArrayList <String> word1 = new ArrayList <String> ();
    	  ArrayList <String> word2 = new ArrayList <String> ();
    	  String sql = "SELECT word1, word2, occurrence FROM " + config.getDbCooccurrenceTable();
 	      try {
 	    	    Statement stmt = connection.createStatement();
 	    	    ResultSet rs = stmt.executeQuery(sql);
 	    	    while ( rs.next() ) { 
 	    	      word1.add( rs.getString("word1") ); 
 	    	      word2.add( rs.getString("word2") );
 	    	      occurrence.add( rs.getInt("occurrence") ); 
 	    	    }
 	    	    rs.close();
 	    	    stmt.close();
 	      } catch (Exception e) { System.out.println("Database Exception (computeSupport): " + e); } 
    	  
 	      // loop over all the word pairs
 	      for ( int i=0; i<occurrence.size(); i++ )
 	      {
    	    Words words = new Words ();
    	    words.addWord( word1.get(i) );
    	    words.addWord( word2.get(i) );
    	    words.setSupport( (double) occurrence.get(i) / (double) numDocs );
    	    if ( words.getSupport() >= config.getMinSupport() ) { wordList.add(words); }
 	      }
 	    
    	} else { System.out.println("Database Exception (computeSupport): " + database.getErrorMessage()); }
 
    	return wordList;
    	
    }
 		
}