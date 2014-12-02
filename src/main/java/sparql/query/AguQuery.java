package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import sparql.*;
import util.Format;

import java.util.ArrayList;
import data.Agu;

public class AguQuery extends Endpoints
{
		
	 private String uri;
	 private Format format = new Format ();
	 private ArrayList <Agu> aguData = new ArrayList <Agu> ();
	 
	 public ArrayList <Agu> getAguData () { return aguData; }
	 
	 private int getIndex ( String u )
	 {
	     int index = -1;
		 for ( int i=0; i<aguData.size(); i++ )
		 {
			 Agu agu = aguData.get(i);
			 if ( agu.getUri() == u ) {
				index = i;
				break;
			 }  
		 }
		 return index;
	 }
	 
	 private boolean haveUri ( String u )
	 {
		 boolean found = false;
		 for ( int i=0; i<aguData.size(); i++ )
		 {
			 Agu agu = aguData.get(i);
			 if ( agu.getUri() == u ) {
			   found = true;
			   break;
			 }
		 }
		 return found;
	 }
	 
	 private String createURI ( String year ) {
	
		return "<http://abstracts.agu.org/meetings/" + year + "/FM>";
		 
	 }
	 
     public static void main(String[] args) {

    	 AguQuery agu = new AguQuery ();
    	 agu.submitQuery( args[0] );
    	 ArrayList <Agu> aguResults = agu.getAguData();
    	 for ( int i=0; i<aguResults.size(); i++ )
    	 {
    		 Agu a = aguResults.get(i);
    		 System.out.println( a.getAbstract() );
    	 }
	 
     }

	 public void submitQuery( String year ) {
	 
		 uri = createURI(year);
		 
		 String sparqlQueryString = 
		      "PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#> " + 
			  "PREFIX agu: <http://abstracts.agu.org/ontology#> " + 
			  "PREFIX swrc: <http://swrc.ontoware.org/ontology#> " + 
              "PREFIX tw: <http://tw.rpi.edu/schema/> " +  
              "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
			  "select ?abstract ?text where { " + 
              "  ?section swc:isSubEventOf " + uri + " . " + 
			  "  ?section agu:section <http://abstracts.agu.org/sections/IN> . " +  
			  "  ?abstract swc:relatedToEvent ?section . " + 
              "  ?abstract a agu:Abstract . " +  
              "  ?abstract swc:hasTopic ?keyword . " + 
              "  ?abstract swrc:abstract ?text " + 
			  "  FILTER(  " + 
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1924\" || " +  
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1938\" || " +  
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1958\" || " +  
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1968\" || " +  
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1970\" || " +  
			  "     STR(?keyword) = \"http://abstracts.agu.org/keywords/1972\" " +  
			  " ) " + 
	 		  "}";
		 
		 ResultSet results = queryEndpoint( this.agu, sparqlQueryString );
		 while (results.hasNext())
    	 {
    		 QuerySolution soln = results.nextSolution();
    		 RDFNode abstractText = soln.get("?text");
    		 RDFNode abstractUri = soln.get("?abstract");
    		 //RDFNode abstractAuthor = soln.get("?name");
    		
    		 //String name = format.removeDataType(abstractAuthor.toString());
             //String[] names = name.split(",");
             
    		 // we are going to get the same uri multiple times because
 			 // each result corresponds to a co-author 
 			 // if we already have this uri then check if we need to add the current
 			 // author. otherwise, create a new object
 			 //if ( haveUri(abstractUri.toString()) )
 			 //{
 				
 			    Agu agu = new Agu ();
 			    agu.setAbstract( format.removeLanguage(abstractText.toString()) );
 			    agu.setUri( abstractUri.toString() );
 	           // agu.addAuthor(names[1],names[0]); // names is Last Name, First Initial
 			    aguData.add(agu);
 			    
 			 //} else {
 			   
 			   // int index = getIndex( uri.toString() );
 			   // Agu currentAgu = aguData.get(index);
 			   // if ( !currentAgu.authorExists(names[1],names[0]) )
 			   // {
 			   //   currentAgu.addAuthor(names[1],names[0]);
 			   // }
 			   // aguData.remove(index);
 			   // aguData.add(currentAgu);
 			  
 			 //} // end else
    	 
    	 } // end while
		 
	 }
	 	
}