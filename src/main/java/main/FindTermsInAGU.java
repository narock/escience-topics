package main;

import java.util.ArrayList;
import data.Agu;
import sparql.query.AguQuery;

public class FindTermsInAGU {
		
	public static void main (String[] args) {
	
		FindTermsInAGU c = new FindTermsInAGU ();
		c.compute( args[0] );
		
	}
	

	private boolean checkForTerms( String text, ArrayList <String> list ) {
		
		boolean result = false;
		text = text.toLowerCase();
		for ( int i=0; i<list.size(); i++ ) {
			if ( text.contains( list.get(i).toLowerCase() ) ) { result = true; }
		}
		return result;
		
	}
	
	public void compute (String year) {
	
		// terms we are interested in
		ArrayList <String> data = new ArrayList <String> ();
		data.add("data set");
		data.add("dataset");
		data.add("datasets");
		ArrayList <String> annotation = new ArrayList <String> ();
		annotation.add("semantic annotation");
		ArrayList <String> domain = new ArrayList <String> ();
		domain.add("conceptual domain model");
		domain.add("domain model");
		domain.add("knowledge base");
		domain.add("knowledge bases");
		ArrayList <String> rdf = new ArrayList <String> ();
		rdf.add("rdf data");
		rdf.add("rdf");
		rdf.add("rdf triple");
		rdf.add("rdf triples");
		ArrayList <String> semweb = new ArrayList <String> ();
		semweb.add("Semantic Web Technologies");
		semweb.add("semantic web");
		semweb.add("semantic web technology");
		semweb.add("semantic");
		semweb.add("application");
		semweb.add("tool");
		semweb.add("platform");
		semweb.add("benchmark");
		semweb.add("semantic web application");
		semweb.add("owl");
		ArrayList <String> ontology = new ArrayList <String> ();
		ontology.add("ontology");
		ontology.add("ontologies");
		ontology.add("semantic web ontology");
		ontology.add("owl dl ontology");
		ArrayList <String> linkedData = new ArrayList <String> ();
		linkedData.add("Linked Data");
		linkedData.add("linked open data");
		linkedData.add("linked data");
		linkedData.add("open data lod");
		linkedData.add("open data source");
		linkedData.add("open data cloud");
		linkedData.add("linked open data cloud");
		ArrayList <String> query = new ArrayList <String> ();
		query.add("sparql");
		query.add("federated query processing");
		query.add("query");
		
		// counters
		int dataCounter = 0;
		int annotationCounter = 0;
		int domainCounter = 0;
		int rdfCounter = 0;
		int semwebCounter = 0;
		int ontologyCounter = 0;
		int linkedDataCounter = 0;
		int queryCounter = 0;
		
		// Get data from AGU
		AguQuery aguQuery = new AguQuery ();
		aguQuery.submitQuery( year );
		ArrayList <Agu> aguData = aguQuery.getAguData();
	    System.out.println(" Got " + aguData.size() + " results for " + year); 
					
		// Loop over all the ISWC abstracts for this year
		for ( int i=0; i<aguData.size(); i++ ) {
						
		   System.out.println("  Working on Abstract " + (i+1) + " of " + aguData.size()); 
		   Agu agu = aguData.get(i);
		   String abstrct = agu.getAbstract();
		   if ( checkForTerms( abstrct, data ) ) { dataCounter++; }
		   if ( checkForTerms( abstrct, annotation ) ) { annotationCounter++; }
		   if ( checkForTerms( abstrct, domain ) ) { domainCounter++; }
		   if ( checkForTerms( abstrct, rdf ) ) { rdfCounter++; }
		   if ( checkForTerms( abstrct, semweb ) ) { semwebCounter++; }
		   if ( checkForTerms( abstrct, ontology ) ){ ontologyCounter++; }
		   if ( checkForTerms( abstrct, linkedData ) ) { linkedDataCounter++; }
		   if ( checkForTerms( abstrct, query ) ) { queryCounter++; }
		   
		} // end for
		
		System.out.println("Data: " + dataCounter);
		System.out.println("Annotations: " + annotationCounter);
		System.out.println("Domain: " + domainCounter);
		System.out.println("RDF: " + rdfCounter);
		System.out.println("Semantic Web: " + semwebCounter);
		System.out.println("Ontology: " + ontologyCounter);
		System.out.println("Linked Data: " + linkedDataCounter);
		System.out.println("Query: " + queryCounter);
		
	}
	
}