package data;

import java.util.ArrayList;

public class Iswc {
	
	private String uri;
	private String abstrct;
	private String sha1;
	
	public ArrayList <String> authors = new ArrayList <String> ();
	
	public String getAbstract () { return abstrct; }
	public ArrayList <String> getAuthors () { return authors; }
	public String getUri() { return uri; }
	public String getSha1() { return sha1; }
	
	public void setAbstract ( String a ) { abstrct = a; }
	public void addAuthor ( String firstName, String lastName ) { 
		String name = firstName.trim() + " " + lastName.trim();
		authors.add( name );
	}
	public void setUri ( String u ) { uri = u; }
	public void setSha1 ( String sh ) { sha1 = sh; }
	
	public boolean authorExists ( String firstName, String lastName ) {
	
		boolean result = false;
		
		// check first initial and last name as checking 
		// first and last name will obviously not work
		// for cases such as Jerome Euzenat and JŽr™me Euzenat
		String author;
		String[] parts;
		String inName;
		String authorName;
		String authorInitial;
		String initial = firstName.trim().substring(0, 1);
		
		inName = initial + lastName.trim();
		for ( int i=0; i<authors.size(); i++ ) {
		   author = authors.get(i);
		   parts = author.split(" ");
		   authorInitial = parts[0].trim().substring(0, 1);
		   authorName = authorInitial + parts[1].trim();
		   if ( authorName.equals(inName) ) { result = true; }
		}
		
		return result;
		
	}
	
}