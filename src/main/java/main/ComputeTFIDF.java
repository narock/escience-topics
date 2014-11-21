package main;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileWrite;

public class ComputeTFIDF {
	
	private HashMap <String, Double> tfidf = new HashMap <String, Double> ();
	private HashMap <String, Integer> docFreq = new HashMap <String, Integer> ();
	
	public HashMap <String, Double> getTFIDF () { return tfidf; }
	
	public static void main (String[] args) {
	
		ComputeTFIDF c = new ComputeTFIDF ();
		try {
			c.compute( args[0] );
			HashMap <String, Double> tfidf = c.getTFIDF();
			HashMap <String, Double> sortedMap = c.sortByValue(tfidf);
			c.writeSortedHash( sortedMap, args[1] );
		} catch ( IOException e ) { System.out.println(e); }
		
	}
	
	private void writeSortedHash ( HashMap <String, Double> sortedMap, String outputFile ) {
		
		FileWrite fw = new FileWrite ();
		Set set = sortedMap.entrySet();
		Iterator iterator = set.iterator();
		while ( iterator.hasNext() ) {
		    Map.Entry <String,Double> me = (Map.Entry) iterator.next();
		    fw.append(outputFile, me.getKey() + ":" + me.getValue());
		}
		
	}
	
	private HashMap <String, Double> sortByValue ( HashMap <String, Double> map ) {

		// code modified from example at: http://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
		
		List list = new LinkedList (map.entrySet());
		 
		// Define Custom Comparator here
		Collections.sort(list, new Comparator() {
		      public int compare(Object o1, Object o2) {
		         return ((Comparable) ((Map.Entry) (o1)).getValue())
		            .compareTo(((Map.Entry) (o2)).getValue());
		      }
		});
		 
		// Copy the sorted list in HashMap using
		// LinkedHashMap to preserve the insertion order
		 HashMap <String, Double> sortedHashMap = new LinkedHashMap <String, Double> ();
		 for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry<String,Double> entry = (Map.Entry<String,Double>) it.next();
		        sortedHashMap.put(entry.getKey(), entry.getValue());
		 } 
		 
		 return sortedHashMap;
	
	}

	
	public void compute (String directory) throws IOException {
	
		// compute the number of documents
		// this is the total number of files in the directory minus 1
		// the minus 1 is due to the docFreq.txt file being in the same directory
		File dir = new File (directory);
		File[] files = dir.listFiles();
		double numDocs = files.length - 1;
		 
		// read the docFreq.txt file
		File docFreqFile = new File (directory + "docFreq.txt");
		FileInputStream fis = new FileInputStream( docFreqFile );			 
	    BufferedReader br = new BufferedReader( new InputStreamReader(fis) );
		String line = null;
	    String[] parts = null;
	    while ((line = br.readLine()) != null) { 
	    	parts = line.split(":");
	    	
	    	// sometimes we run into :// in front a of term
			if ( parts.length == 3 ) {
			  parts[0] = parts[1].replace("//", "");
			  parts[1] = parts[2];
			}
			  
	    	docFreq.put( parts[0], Integer.valueOf(parts[1]) );
	    }	 
	    br.close();
	
		// read all the term frequency files
	    double tf;
	    double idf;
	    double tfidfThisDocument;
	    double docsContaining;
		for ( int i=0; i<numDocs; i++ ) {
			
		  System.out.println("Reading " + files[i].toString() + ": " + (i+1) + " of " + numDocs);
		  
		  if ( files[i].getName().contains("iswc_") ) {
		  
			  fis = new FileInputStream( files[i] );
			  br = new BufferedReader( new InputStreamReader(fis) );
			  while ((line = br.readLine()) != null) { 
				  
				  parts = line.split(":");
				  
				  // sometimes we run into :// in front a of term
				  if ( parts.length == 3 ) {
					parts[0] = parts[1].replace("//", "");
					parts[1] = parts[2];
				  }
				  
				  // use logarithmically scaled frequency: tf(t,d) = 1 + log f(t,d), or zero if f(t, d) is zero;
				  tf = 1d + Math.log10( Double.valueOf(parts[1]) );
				  docsContaining = Double.valueOf( docFreq.get(parts[0]) );
				  idf = Math.log10( numDocs / docsContaining );
				  
				  tfidfThisDocument = tf*idf; 
			      // we just computed tfidf for this document
				  // what we ultimately want is TFIDF, which is
				  // the sum of tfidf accross all documents in this year
				  // if we already have this term in the hashmap then 
				  // continue to sum the tfidf values
				  double currentSum = 0;
				  if ( tfidf.containsKey(parts[0]) ) { currentSum = tfidf.get(parts[0]); } 
				  tfidf.put( parts[0], currentSum + tfidfThisDocument );
				  
			  }	 
			  br.close();
		  } // end if
		  
		} // end for
	}
	
}