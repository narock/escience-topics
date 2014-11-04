package opennlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import util.Configuration;
import java.util.ArrayList;

public class NLP {
	
	private SentenceDetector sentDetector = null;
	private POSTagger posTagger = null;
	private Tokenizer tokenizer = null;
	
	public String[] getSentences ( String paragraph ) { return sentDetector.sentDetect( paragraph ); }
	
	public String[] getTokens ( String sentence ) { return tokenizer.tokenize( sentence ); }
	
	public String[] getPOS ( String[] tokens ) { return posTagger.tag( tokens ); }
	
	public ArrayList <String> getNouns( String[] tokens, String[] pos ) {
		
		ArrayList <String> nouns = new ArrayList <String> ();
	    for ( int i=0; i<tokens.length; i++ ) {
	    	
	      if ( pos[i].equals("NN") || pos[i].equals("NNS") || pos.equals("NNP") ) { nouns.add( tokens[i] ); }
	   	 
	    }
	    return nouns;
	    
	}

	public void initialize ( Configuration config ) {
	
		try {
			
		    sentDetector = new SentenceDetectorME( config.getSentenceModel() );
		    posTagger = new POSTaggerME( config.getPosModel() );
		    tokenizer = new TokenizerME( config.getTokenizerModel() );
			   
		} catch (Exception e) { System.out.println(e.toString()); }
	
	
	}
}