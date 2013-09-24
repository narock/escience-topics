package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import sparql.*;
import java.util.ArrayList;
import data.Iswc;

public class IswcQuery
{
	
	 private String endpoint = Endpoints.iswc;
	 private ArrayList <String> uris = new ArrayList <String> ();
	 private ArrayList <Iswc> iswcData = new ArrayList <Iswc> ();
	 
	 public ArrayList <Iswc> getIswcData () { return iswcData; }
	 
	 public IswcQuery () {
		 
		 // International Semantic Web Conferences 2007-2012 Proceedings
		 uris.add("<http://data.semanticweb.org/conference/iswc-aswc/2007/proceedings>");
		 uris.add("<http://data.semanticweb.org/conference/iswc/2008/proceedings>");
		 uris.add("<http://data.semanticweb.org/conference/iswc/2009/proceedings>");
		 uris.add("<http://data.semanticweb.org/conference/iswc/2010/proceedings>");
		 uris.add("<http://data.semanticweb.org/conference/iswc/2011/proceedings>");
		 uris.add("<http://data.semanticweb.org/conference/iswc/2012/proceedings>");
	 }
     public static void main(String[] args) {

    	 IswcQuery iswc = new IswcQuery ();
    	 //iswc.testEndpoint( );
    	 iswc.submitQuery( );
	 
     }

     private String getQueryString ( String uri ) {
    
    	 String sparqlQueryString = 
				   " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				   " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				   " PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
				   " PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
				   " PREFIX dcterms: <http://purl.org/dc/terms/> " +
				   " PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				   " PREFIX swrc: <http://swrc.ontoware.org/ontology#> " +
				   " PREFIX swrc-ext: <http://www.cs.vu.nl/~mcaklein/onto/swrc_ext/2005/05#> " +
				   " PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> " +
				   " PREFIX ical: <http://www.w3.org/2002/12/cal/ical#> " +
				   " PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#> " +
				   " SELECT DISTINCT ?s ?a ?first ?last ?sha " +
				   "	WHERE { " +
				   "		?s swc:isPartOf " + uri + " . " +
				   "	 	?s swrc:abstract ?a . " +
				   "		?s foaf:maker ?au . " +
				   "		?au foaf:firstName ?first . " +
				   "        ?au foaf:mbox_sha1sum ?sha . " +
				   "		?au foaf:lastName ?last " +
				   "    } ";
    	 return sparqlQueryString;
    	 
     }
     
	 public void submitQuery( ) {
	        
		 for ( int i=0; i<uris.size(); i++ ) {
			 
			 Iswc iswc = new Iswc ();
			 ResultSet results = Endpoints.queryEndpoint( endpoint, getQueryString(uris.get(i)) );
			 while ( results.hasNext() )
			 {
				 QuerySolution soln = results.nextSolution();
				 RDFNode uri = soln.get("?s");
				 RDFNode abstr = soln.get("?a");
				 RDFNode firstName = soln.get("?first");
				 RDFNode lastName = soln.get("?last");
				 RDFNode sha = soln.get("?sha");
				 iswc.setSha1( sha.toString() );
				 iswc.setAbstract( abstr.toString() );
				 iswc.setUri( uri.toString() );
				 
				 // ISWC data is very detailed including multiple versions
				 // of authors name - such as Jerome Euzenat and JŽr™me Euzenat
				 // we want to avoid double counting and also we only have
				 // first initial and last name with AGU data
				 // here we check that we insert any given author only once
				 if ( !iswc.authorExists(firstName.toString(), lastName.toString()) )
				 {
				   iswc.addAuthor(firstName.toString(), lastName.toString());
				 }
			 }
			 iswcData.add( iswc );
		 
		 } // end for loop over all uris

	 }

	 public void testEndpoint() { Endpoints.testEndpoint( endpoint ); }

}