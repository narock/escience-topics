package main;

import util.Configuration;
import util.ConfigurationParser;
import java.util.HashMap;
import java.util.ArrayList;

import mining.TopicAnalysis;
import sparql.query.*;
import data.*;

public class RunTopicAnalysis {
	
	public static void main (String[] args) {
			
		// Run the configuration code
		Configuration config = null;
		ConfigurationParser parser = new ConfigurationParser ();
		try { 
			config = parser.parse(args[0]);
			config.initialize(); 
		} catch (Exception e) { System.out.println(e); }
		
		// If not errors in configuration then continue
		if ( !config.isError() ) {
						
			// Get data from ISWC
			if ( config.verbose() ) { System.out.println("Querying ISWC SPARQL Endpoint..."); }
			IswcQuery iswcQuery = new IswcQuery ();
			iswcQuery.submitQuery(config.getYear());
			ArrayList <Iswc> iswcData = iswcQuery.getIswcData();
			if ( config.verbose() ) { System.out.println(iswcData.size() + " ISWC results for " + config.getYear()); }
						
			// Create and initialize the text analyzer for the ISWC data
			if ( config.verbose() ) { System.out.println("Configuring NLP for topic analysis..."); }
			TopicAnalysis text = new TopicAnalysis ( config, iswcData.size() );
			
			// Loop over all the ISWC abstracts for this year
			for ( int i=0; i<iswcData.size(); i++ )
			{
				Iswc iswc = iswcData.get(i);
				String[] sentences = text.getSentences( iswc.getAbstract() );		// parse the abstract to sentences
				ArrayList <String> nouns = text.parseSentences(sentences);			// extract nouns from sentences
				HashMap <String,String> nounPairs = text.createPairs(nouns);		// find co-occurring noun pairs
				String dbError = text.updateDatabase( config, nounPairs );			// compute occurrence rates
				if ( dbError != null ) {
					ArrayList <Words> supportedWords = text.computeSupport( config ); // keep pairs above min support
					for ( int z=0; z<supportedWords.size(); z++ )
					{
						Words w = supportedWords.get(z);
						ArrayList <String> wds = w.getWords();
						System.out.println(wds.get(0) + " " + wds.get(1) + " " + w.getSupport());
					}
				}			
			} // end for
			
			// Output the status
			
			// repeat for AGU ESSI data
			
		} else {
	
			// Print out the configuration errors
			ArrayList <String> configErrors = config.getErrors();
			for ( int i=0; i<configErrors.size(); i++ ) { System.out.println(configErrors.get(i)); }
		
		}
		
	}
	
}