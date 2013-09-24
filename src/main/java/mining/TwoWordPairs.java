package mining;

import java.util.HashMap;
import java.util.ArrayList;

public class TwoWordPairs {
	
	public HashMap <String,String> createPairs ( ArrayList <String> words ) {
		
		HashMap <String,String> pairs = new HashMap <String,String> ();
		for ( int i=0; i<words.size(); i++ ) {
		   for ( int j=1; j<words.size(); j++ ) { pairs.put( words.get(i), words.get(j) ); }
		}
		return pairs;
		
	}
	
}