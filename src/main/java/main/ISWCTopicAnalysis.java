package main;

import util.Configuration;
import util.ConfigurationParser;
import java.util.ArrayList;
import java.io.File;
import opennlp.NLP;
import sparql.query.*;
import data.*;
import util.FileWrite;

public class ISWCTopicAnalysis {
	
	public static void main (String[] args) {
			
		// Run the configuration code
		Configuration config = null;
		ConfigurationParser parser = new ConfigurationParser ();
		try { 
			config = parser.parse(args[0]);
			config.initialize(); 
		} catch (Exception e) { System.out.println(e); }
		
		// Second input is the year, Third input is output director
		int year = Integer.valueOf( args[1] );
		String outputDir = args[2] + File.separator + args[1] + File.separator;
		
		// If not errors in configuration then continue
		if ( !config.isError() ) {
						
			// Create a File Writer
			FileWrite file = new FileWrite ();
			
			// Get data from ISWC
			if ( config.verbose() ) { System.out.println("Querying ISWC SPARQL Endpoint..."); }
			IswcQuery iswcQuery = new IswcQuery ();
			iswcQuery.submitQuery( iswcQuery.getQuery(year) );
			ArrayList <Iswc> iswcData = iswcQuery.getIswcData();
			if ( config.verbose() ) { System.out.println(" Got " + iswcData.size() + " results for " + year); }
						
			// Create and initialize the NLP object
			if ( config.verbose() ) { System.out.println("Configuring NLP..."); }
			NLP openNLP = new NLP ();
			openNLP.initialize( config );
			
			// Loop over all the ISWC abstracts for this year
			for ( int i=0; i<iswcData.size(); i++ )
			{
				
				if ( config.verbose() ) { System.out.println("  Working on Abstract " + (i+1) + " of " + iswcData.size()); }
				Iswc iswc = iswcData.get(i);
				String[] parts = iswc.getUri().split("/");
				String abstractOutputFile = outputDir + "iswc_abstract_" + String.valueOf(year) + 
					"_" + parts[ parts.length-1 ] + ".txt";
				file.append( abstractOutputFile, iswc.getAbstract() );
				String[] sentences = openNLP.getSentences( iswc.getAbstract() );		// parse the abstract to sentences
				
				// loop over all the sentences
				for ( int j=0; j<sentences.length; j++ ) {
					
					String[] tokens = openNLP.getTokens( sentences[j] ); // break the sentence into tokens
					
					// get Part of Speech for each token
					String[] pos = openNLP.getPOS( tokens );
					
					// get nouns and noun phrases
					ArrayList <String> nouns = openNLP.getNouns( tokens, pos );
					
					// write them out to a file
					String outputFile = outputDir + "iswc_nouns_" + String.valueOf(year) + "_" + parts[ parts.length-1 ] + ".txt";
					for ( int k=0; k<nouns.size(); k++ ) { file.append( outputFile, nouns.get(k) ); }
					
				}
					
			} // end for loop over all abstracts for this year
			
		} else {
	
			// Print out the configuration errors
			ArrayList <String> configErrors = config.getErrors();
			for ( int i=0; i<configErrors.size(); i++ ) { System.out.println(configErrors.get(i)); }
		
		}
		
	}
	
}