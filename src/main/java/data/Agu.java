package data;

import java.util.ArrayList;

public class Agu {
	
	public String abstrct;
	public String uri;
	
	// holds first initial and last name
	public ArrayList <String> authors = new ArrayList <String> ();

	public ArrayList <String> getAuthors () { return authors; }
	public String getAbstract () { return abstrct; }
	public String getUri() { return uri; }
	
	public void setAbstract ( String a ) { abstrct = a; }
	public void setUri ( String u ) { uri = u; }
	public void addAuthor ( String firstInitial, String lastName ) { 
		String name = firstInitial.trim() + " " + lastName.trim();
		authors.add( name );
	}
	
	public boolean authorExists ( String firstInitial, String lastName ) {
		
		boolean result = false;
		
		// check first initial and last name as checking 
		// first and last name will obviously not work
		// for cases such as Jerome Euzenat and JŽr™me Euzenat
		String author;
		String inName = firstInitial.trim() + lastName.trim();
		for ( int i=0; i<authors.size(); i++ ) {
		   author = authors.get(i);
		   if ( author.equals(inName) ) { result = true; }
		}
		
		return result;
		
	}
	
}