package util;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;

public class HashMapSort {

	public void writeSortedHash ( HashMap <String, Integer> sortedMap, String outputFile ) {
	
		FileWrite fw = new FileWrite ();
		Set set = sortedMap.entrySet();
		Iterator iterator = set.iterator();
		while ( iterator.hasNext() ) {
		    Map.Entry <String,Integer> me = (Map.Entry) iterator.next();
		    fw.append(outputFile, me.getKey() + ":" + me.getValue());
		}
		
	}
	
	public HashMap <String, Integer> sortByValue ( HashMap <String, Integer> map ) {

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
		 HashMap <String, Integer> sortedHashMap = new LinkedHashMap <String, Integer> ();
		 for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>) it.next();
		        sortedHashMap.put(entry.getKey(), entry.getValue());
		 } 
		 
		 return sortedHashMap;
	
	}
	
}
