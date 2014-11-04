package util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

public class Configuration {
	
	// Configuration Errors
	ArrayList <String> errorMessages = new ArrayList <String> ();
	
	// Verbose (output current status)
	private boolean vbs = false;
	
	// Open NLP configuration
	private String nlpModelPath = null;
	private SentenceModel sentenceModel = null;
	private TokenizerModel tokenizerModel = null;
	private POSModel posModel = null;
		
	public boolean isError () 
	{
		if ( errorMessages.size() > 0 ) { return true; } else { return false; }
	}
	
	public void initialize ()
	{

		// get the opennlp file locations from the maven resources directory
		HashMap <String,String> files = new HashMap <String,String> ();
		files.put("sentenceModel", nlpModelPath + "en-sent.bin");
		files.put("tokenizerModel", nlpModelPath + "en-token.bin");
		files.put("posModel", nlpModelPath + "en-pos-maxent.bin");
		Iterator <Map.Entry<String,String>> it = files.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry <String,String> pairs = (Map.Entry<String,String>) it.next();
			InputStream in = getClass().getClassLoader().getResourceAsStream( pairs.getValue() );
			String key = pairs.getKey();
			try {
				if ( key.equals("sentenceModel") ) { sentenceModel = new SentenceModel( in ); }
				if ( key.equals("posModel") ) { posModel = new POSModel( in ); }  
				if ( key.equals("tokenizerModel") ) { tokenizerModel = new TokenizerModel( in ); }
				it.remove(); // avoids a ConcurrentModificationException
			} catch (Exception e) { errorMessages.add(e.toString()); }
	    }
		
	}
	
	// Getter methods
	public SentenceModel getSentenceModel () { return sentenceModel; }
	public TokenizerModel getTokenizerModel () { return tokenizerModel; }
	public POSModel getPosModel () { return posModel; }
	public ArrayList <String> getErrors () { return errorMessages; }
	public String getNLPmodelPath () { return nlpModelPath; }
	public boolean verbose () { return vbs; }
	
	// Setter methods
	public void setNLPmodelPath ( String s ) { nlpModelPath = s; }
	public void setVerbose ( boolean v ) { vbs = v; }
	
	public static void main (String[] args)
	{
		Configuration config = null;
		ConfigurationParser parser = new ConfigurationParser ();
		try { 
			config = parser.parse(args[0]);
			config.initialize(); 
		} catch (Exception e) { System.out.println(e); }
		
		if ( config.isError() )
		{
			ArrayList <String> errors = config.getErrors();
			for ( int i=0; i<errors.size(); i++ ) { System.out.println(errors.get(i)); }
		} else {
			System.out.println("NLP Sentence Model: " + config.getNLPmodelPath() + "en-sent.bin");
			System.out.println("NLP Tokenizer Model: " + config.getNLPmodelPath() + "en-token.bin");
			System.out.println("NLP POS Model: " + config.getNLPmodelPath() + "en-pos-maxent.bin");
			System.out.println(" ");
		}
	
	}
	
}