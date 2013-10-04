package util;

public class Format 
{
	
	public String removeLanguage ( String s )
	{
		String[] parts = s.split("@");
		return parts[0];
	}
	
	public String removeDataType ( String s ) 
	{
		String[] parts = s.split("\\^");
		return parts[0];
	}
	
}