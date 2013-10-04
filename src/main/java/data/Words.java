package data;

import java.util.ArrayList;

public class Words {
	
	private double support;
	private ArrayList <String> words = new ArrayList <String> ();
	
	public double getSupport () { return support; }
	public ArrayList <String> getWords () { return words; }
	
	public void setSupport ( double s ) { support = s; }
	public void addWord ( String s ) { words.add(s); }
	
}