package main;

import java.io.File;

import util.ReadNounFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import util.HashMapSort;
public class CalculateTFandDF {
	
	public static void main ( String[] args ) {
		
		// HashMap to keep track of TF and DF
		HashMapSort sorter = new HashMapSort ();
		HashMap <String, Integer> tf = new HashMap <String, Integer> ();
		HashMap <String, Integer> df = new HashMap <String, Integer> ();
		
		// Get all the file names from the input directory
		File dir = new File ( args[0] );
		File[] files = dir.listFiles();
		
		// Input 2 is the output directory
		String outputDir = args[1] + File.separator;
		String dfOutputFile = outputDir + "docFreq.txt";
		
		// Read each file (abstract nouns and noun phrases)
		ReadNounFile reader = new ReadNounFile ();
		for ( int i=0; i<files.length; i++ ) {
			
			try { 
			
				String tfOutputFile = outputDir + files[i].getName();
				reader.read( files[i] ); 
				ArrayList <String> uniqueTerms = new ArrayList <String> ();
				ArrayList <String> terms = reader.getTerms();
				
				// Keep track of TF and DF
				String currentTerm = null;
				for ( int j=0; j<terms.size(); j++ ) {
				
					int count = 1;
					currentTerm = terms.get(j);
					
					// if we already have the term then increment the counter by 1
					if ( tf.containsKey(currentTerm) ) { count = tf.get(currentTerm) + 1; }
					
					// put the new value in the hash maps
					tf.put( currentTerm, count );
				
					// keep track of the unique terms in this abstract
					if ( !uniqueTerms.contains(currentTerm) ) { uniqueTerms.add(currentTerm); }
					
				} // end for loop over all terms in this abstract
				
				// use the unique terms to update document frequency
				for ( int j=0; j<uniqueTerms.size(); j++ ) { 
					
					int count = 1;
					if ( df.containsKey(uniqueTerms.get(j)) ) { count = df.get(uniqueTerms.get(j)) + 1; }
					df.put( uniqueTerms.get(j), count );
					
				}
				
				// output term frequency from lowest to highest
				HashMap <String, Integer> sortedMap = sorter.sortByValue( tf );				
				sorter.writeSortedHash( sortedMap, tfOutputFile );
				
				// reset the term counter for the next abstract
				tf.clear();
				
			} catch ( IOException e ) { System.out.println( files[i] + ": " + e); }
				 
		} // end for loop over all abstract files
		
		// output the DF results
		HashMap <String, Integer> sortedMap = sorter.sortByValue( df );
		sorter.writeSortedHash( sortedMap, dfOutputFile );
		
	}
	
}