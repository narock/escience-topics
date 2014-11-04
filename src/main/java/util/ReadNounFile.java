package util;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadNounFile {
	
	private ArrayList <String> terms = new ArrayList <String> ();
	
	public ArrayList <String> getTerms () { return terms; }
	
	public void read (File file) throws IOException {
	
		FileInputStream fis = new FileInputStream( file );
	 
		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader( new InputStreamReader(fis) );
	 
		String line = null;
		while ((line = br.readLine()) != null) { terms.add( line.trim() ); }	 
		br.close();
		
	}
	
}