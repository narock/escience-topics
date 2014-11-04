package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import sparql.*;

import java.util.ArrayList;

import data.Iswc;

public class IswcQuery extends Endpoints
{
	
	 private ArrayList <Iswc> iswcData = new ArrayList <Iswc> ();
	 
	    // SELECT DISTINCT ?s ?a ?firstName ?lastName
		//	 WHERE { 
		//	 	?s swc:isPartOf <http://data.semanticweb.org/conference/iswc/2009/proceedings> . 
		//	 	?s swrc:abstract ?a . 
		//	 	?s foaf:maker ?au  .
		//	         ?au foaf:firstName ?firstName .
		//	         ?au foaf:family_name ?lastName .
		//	 } limit 12
			 
	 public String getQuery ( int year ) {
		 
		 String query = null;
		 
		 switch ( year ) {
		 	case 2009: query =
		 
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
			   " SELECT DISTINCT ?s ?a " +
			   "	WHERE { " +
			   "		?s swc:isPartOf " + "<http://data.semanticweb.org/conference/iswc/2009/proceedings> . " +
			   "	 	?s swrc:abstract ?a . " +
			   "    } ";
		 	   break;
		 	
		 	case 2010: query = 
	 
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
			   " SELECT DISTINCT ?s ?a " +
			   "	WHERE { " +
			   "		?s swc:isPartOf " + "<http://data.semanticweb.org/conference/iswc/2010/proceedings> . " +
			   "	 	?s swrc:abstract ?a . " +
			   "		?s foaf:maker ?au  " +
			   "    } ";
		 	   break;
		 	 
		 	// NO ISWC DATA AVAILABLE   
		 	case 2011: query = null; break;   
	 
		 	case 2012: query =
	 
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
			   " SELECT DISTINCT ?s ?a " +
			   "	WHERE { " +
			   "	   { ?s swc:isPartOf <http://data.semanticweb.org/conference/iswc/2012/proceedings-1> } UNION " + 
			   "	     { ?s swc:isPartOf <http://data.semanticweb.org/conference/iswc/2012/proceedings-2> } .  " + 
			   "	   ?s swrc:abstract ?a . " + 
		       "}";
		 	   break;
		 	   
		 	case 2013: query = 

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
			   " SELECT DISTINCT ?s ?a " +
			   "	WHERE { " +
			   "	   { ?s swc:isPartOf <http://data.semanticweb.org/conference/iswc/2013/proceedings-1> } UNION " + 
			   "	     { ?s swc:isPartOf <http://data.semanticweb.org/conference/iswc/2013/proceedings-2> } .  " + 
			   "	   ?s swrc:abstract ?a . " + 
		       "}";
		 	   break;
		 	   
		 }
		 
		 return query;
	 
	 }
	 
	 public ArrayList <Iswc> getIswcData () { return iswcData; }
	 
	 
     public static void main(String[] args) {

    	 IswcQuery iswcQuery = new IswcQuery ();
    	 iswcQuery.submitQuery("2007");
	 
     }
     
	 public void submitQuery( String sparqlQuery ) {
	        
		ResultSet results = queryEndpoint( this.iswc, sparqlQuery );
		while ( results.hasNext() )
		{
			
			QuerySolution soln = results.nextSolution();
			RDFNode uri = soln.get("?s");
			RDFNode abstr = soln.get("?a");
				
			Iswc iswc = new Iswc ();
			iswc.setAbstract( abstr.toString() );
			iswc.setUri( uri.toString() );
			iswcData.add(iswc);

		} // end while
		 
	 }

}