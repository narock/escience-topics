package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import sparql.*;
import java.util.ArrayList;
import data.Agu;

public class AguQuery 
{
		
	 private Format format = new Format ();
 	 private String endpoint = Endpoints.agu;
	 private ArrayList <Agu> aguData = new ArrayList <Agu> ();
	 private ArrayList <String> uris = new ArrayList <String> ();
	 
	 public ArrayList <Agu> getAguData () { return aguData; }
	 
	 public AguQuery () {
	
		 uris.add("<http://abstracts.agu.org/meetings/2007/FM>");
		 uris.add("<http://abstracts.agu.org/meetings/2008/FM>");
		 uris.add("<http://abstracts.agu.org/meetings/2009/FM>");
		 uris.add("<http://abstracts.agu.org/meetings/2010/FM>");
		 uris.add("<http://abstracts.agu.org/meetings/2011/FM>");
		 uris.add("<http://abstracts.agu.org/meetings/2012/FM>");
		 
	 }
	 
     public static void main(String[] args) {

    	 AguQuery agu = new AguQuery ();
    	 agu.testEndpoint();
    	 agu.submitQuery( args[0] );
    	 ArrayList <Agu> aguResults = agu.getAguData();
    	 for ( int i=0; i<aguResults.size(); i++ )
    	 {
    		 Agu a = aguResults.get(i);
    		 System.out.println( a.getAbstract() + " " + a.getSession() );
    	 }
	 
     }

	 public void submitQuery( String name ) {
	        
		 // all agu abstracts for 2012
		 PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#>
			 PREFIX agu: <http://abstracts.agu.org/ontology#>
			 select distinct ?abstract where {
			   ?s swc:isSubEventOf <http://abstracts.agu.org/meetings/2012/FM> .
			   ?s agu:section <http://abstracts.agu.org/sections/IN> .
			   ?abstract swc:relatedToEvent ?s
			 }
	 get authors and email addresses for informatics session
		- if sha not available then get email and compute it
	 
		 String sparqlQueryString = 
				    " prefix dc:      <http://purl.org/dc/terms/>" +
				 	" prefix foaf:    <http://xmlns.com/foaf/0.1/>" +
				 	" prefix tw:      <http://tw.rpi.edu/schema/>" +
				    " prefix swrc:    <http://swrc.ontoware.org/ontology#>" +
				    " prefix swc:     <http://data.semanticweb.org/ns/swc/ontology#>" +
				    " select distinct ?abstract ?name ?stitle ?atitle where {" +
				 	"   ?abstract swc:relatedToEvent ?session ." +
				    "   ?abstract dc:title ?atitle ." +
				    "   ?session swrc:eventTitle ?stitle ." +
				    "   ?abstract tw:hasAgentWithRole ?role ." +
				    "   ?person tw:hasRole ?role ." +
				    "   ?person foaf:name ?name ." +
				    "   FILTER regex(?name, \"" + name + "\")" + 
	 				" } ";
		 ResultSet results = Endpoints.queryEndpoint( endpoint, sparqlQueryString );
		 while (results.hasNext())
    	 {
    		 QuerySolution soln = results.nextSolution();
    		 RDFNode abstractTitle = soln.get("?atitle");
    		 RDFNode sessionTitle = soln.get("?stitle");
    		 RDFNode abstractUri = soln.get("?abstract");
    		 Agu agu = new Agu ();
             agu.setSession( format.removeLanguage( sessionTitle.toString() ) );
             agu.setAbstract( format.removeLanguage( abstractTitle.toString() ) ); 
             agu.setUri( abstractUri.toString() );
             aguData.add(agu);
    	 }
		 
	 }
	 
	 public void testEndpoint() { Endpoints.testEndpoint( endpoint ); }
	
}