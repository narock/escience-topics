package test;

import java.util.Vector;

public class StopWords {
	
	private String[] stopWords = { "a", "able", "about", "across", "after", "all", "almost", "also", "am", 
								   "among", "an", "and", "any", "are", "as" , "at", "be", "because", "been", 
								   "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", 
								   "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", 
								   "he", "her", "hers", "him", "his", "how", "however", "I", "if", "in", "into", 
								   "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", 
								   "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", 
								   "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", 
								   "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", 
								   "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", 
								   "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", 
								   "will", "with", "would", "yet", "you", "your" };
	
	public Vector <String> removeStopWords (Vector <String> input) {
		Vector <Integer> indicies = new Vector <Integer> ();
		Vector <String> output = new Vector <String> ();
		boolean found;
		 for (int i=0; i<input.size(); i++) {
			found = false;
		    for (int j=0; j<stopWords.length; j++) { 
		      // compare to stop words and ignore case
		      // we do not removed the stop words at this step
		      // because it would affect the indexing
		      if ( input.get(i).compareToIgnoreCase(stopWords[j]) == 0 ) { found = true; } 
		    }
		    if ( !found ) { indicies.add(i); }
		 }
		 for (int k=0; k<indicies.size(); k++) { output.add( input.get(indicies.get(k)) ); }
		return output;
		
	}
	
	public Vector <String> createVector (String s) {
		
		Vector <String> doc = new Vector <String> ();
		String[] parts = s.split(" ");
		for (int i=0; i<parts.length; i++) { 
			// replace common punctuation
			parts[i] = parts[i].replace(")", "");
			parts[i] = parts[i].replace("(", "");
			parts[i] = parts[i].replace("-", " ");
			parts[i] = parts[i].replace(",", "");
			parts[i] = parts[i].replace(":", "");
			parts[i] = parts[i].replace("\\.", "");
			parts[i] = parts[i].replace("\"", "");
			parts[i] = parts[i].replace("'ve", " have");
			parts[i] = parts[i].replace("'s", "");
			// did we convert "we've" to "we have"
			// we and have are both stop words so 
			// don't add either
			if (!parts[i].trim().equals("we have")) { doc.add(parts[i]); }
		}
		return doc;

	}

}