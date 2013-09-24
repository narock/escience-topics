package nlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import util.Configuration;
import org.tartarus.snowball.ext.PorterStemmer;
import java.util.ArrayList;

public class AnalyzeText {
	
	private SentenceDetector sentDetector = null;
	private POSTagger posTagger = null;
	private Tokenizer tokenizer = null;
	
	public String[] getSentences ( String paragraph ) { return sentDetector.sentDetect( paragraph ); }
	
	public String[] getTokens ( String sentence ) { return tokenizer.tokenize( sentence ); }
	
	public String[] getPOS ( String[] tokens ) { return posTagger.tag( tokens ); }
	
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
	
	public void initialize ( Configuration config ) {
	
		try {
			
		    sentDetector = new SentenceDetectorME( config.getSentenceModel() );
		    posTagger = new POSTaggerME( config.getPosModel() );
		    tokenizer = new TokenizerME( config.getTokenizerModel() );
			   
		} catch (Exception e) { System.out.println(e.toString()); }
	
	}
}