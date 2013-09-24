package main;

import util.Configuration;
import util.ConfigurationParser;

import java.util.ArrayList;
import nlp.AnalyzeText;
import sparql.query.*;
import data.*;

public class TopicAnalysis {
	
	public static void main (String[] args) {
	
		// Run the configuraiton code
		Configuration config = null;
		ConfigurationParser parser = new ConfigurationParser ();
		try { 
			config = parser.parse(args[0]);
			config.initialize(); 
		} catch (Exception e) { System.out.println(e); }
		if ( !config.isError() ) {
			
			// Create and initiale the text analyzer
			AnalyzeText text = new AnalyzeText ();
			text.initialize(config);
			
			// Get data from ISWC
			IswcQuery iswcQuery = new IswcQuery ();
			iswcQuery.submitQuery();
			ArrayList <Iswc> iswcData = iswcQuery.getIswcData();
			
			// Parse the abstracts, stem, and get the nouns
			Iswc iswc = iswcData.get(0);
			String[] sentences = text.getSentences( iswc.getAbstract() );
			ArrayList <String> nouns = text.parseSentences( sentences );
			for ( int i=0; i<nouns.size(); i++ ) {
				System.out.println( nouns.get(i) );
			}
			
		} else {
	
			ArrayList <String> configErrors = config.getErrors();
			for ( int i=0; i<configErrors.size(); i++ ) { System.out.println(configErrors.get(i)); }
		
		}
	}
	
}