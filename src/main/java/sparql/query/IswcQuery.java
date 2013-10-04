package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import sparql.*;
import java.util.ArrayList;
import data.Iswc;

public class IswcQuery extends Endpoints
{
	
	 private String uri;
	 private ArrayList <Iswc> iswcData = new ArrayList <Iswc> ();
	 
	 public ArrayList <Iswc> getIswcData () { return iswcData; }
	 
	 private int getIndex ( String u )
	 {
	     int index = -1;
		 for ( int i=0; i<iswcData.size(); i++ )
		 {
			 Iswc iswc = iswcData.get(i);
			 if ( iswc.getUri() == u ) {
				index = i;
				break;
			 }  
		 }
		 return index;
	 }
	 
	 private boolean haveUri ( String u )
	 {
		 boolean found = false;
		 for ( int i=0; i<iswcData.size(); i++ )
		 {
			 Iswc iswc = iswcData.get(i);
			 if ( iswc.getUri() == u ) {
			   found = true;
			   break;
			 }
		 }
		 return found;
	 }
	 
	 private String createURI ( String year ) {
		 
		 // International Semantic Web Conferences 2007-2012 Proceedings
		 uri = "<http://data.semanticweb.org/conference/iswc-aswc/" + year.trim() + "/proceedings>";
		 return uri;
		 
	 }
	 
     public static void main(String[] args) {

    	 IswcQuery iswcQuery = new IswcQuery ();
    	 iswcQuery.submitQuery("2007");
	 
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
     
	 public void submitQuery( String year ) {
	        
		uri = createURI( year );
		ResultSet results = queryEndpoint( this.iswc, getQueryString(uri) );
		while ( results.hasNext() )
		{
			QuerySolution soln = results.nextSolution();
			RDFNode uri = soln.get("?s");
			RDFNode abstr = soln.get("?a");
			RDFNode firstName = soln.get("?first");
			RDFNode lastName = soln.get("?last");
			RDFNode sha = soln.get("?sha");
			String initial = firstName.toString().substring(0, 1); // use first initial to be consistent w/ AGU

			// we are going to get the same uri multiple times because
			// each result corresponds to a co-author or a different version
			// of an existing author's name - such as Jerome Euzenat and JŽr™me Euzenat
			// if we already have this uri then check if we need to add the current
			// author. otherwise, create a new object
			if ( haveUri(uri.toString()) )
			{
				
			  Iswc iswc = new Iswc ();
			  iswc.setSha1( sha.toString() );
			  iswc.setAbstract( abstr.toString() );
			  iswc.setUri( uri.toString() );
			  iswc.addAuthor(initial.trim(), lastName.toString());
			  iswcData.add(iswc);
			  
			} else {
			  
			  // we want to avoid double - such as Jerome Euzenat and JŽr™me Euzenat
			  // and also we only have first initial and last name with AGU data
			  // here we check that we insert any given author only once
			  // check for existence of first initial last name
			  int index = getIndex( uri.toString() );
			  Iswc currentIswc = iswcData.get(index);
			  if ( !currentIswc.authorExists(firstName.toString(), lastName.toString()) )
			  {
			    currentIswc.addAuthor(initial.trim(), lastName.toString());
			  }
			  iswcData.remove(index);
			  iswcData.add(currentIswc);
			  
			} // end else

		} // end while
		 
	 }

}