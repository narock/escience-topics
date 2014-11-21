package main;

import java.util.ArrayList;
import data.Iswc;
import sparql.query.IswcQuery;

public class ComputeDfAguTopics {
		
	public static void main (String[] args) {
	
		ComputeDfAguTopics c = new ComputeDfAguTopics ();
		c.compute( Integer.valueOf(args[0]) );
		
	}
	

	private boolean checkForTerms( String text, ArrayList <String> list ) {
		
		boolean result = false;
		for ( int i=0; i<list.size(); i++ ) {
			if ( text.contains( list.get(i)) ) { result = true; }
		}
		return result;
		
	}
	
	public void compute (int year) {
	
		// terms we are interested in
		ArrayList <String> logics = new ArrayList <String> ();
		logics.add("formal logics");
		logics.add("formal logic");
		logics.add("logics");
		logics.add("grammers");
		ArrayList <String> kr = new ArrayList <String> ();
		kr.add("knowledge representation");
		kr.add("knowledge base");
		kr.add("knowledge bases");
		ArrayList <String> ont = new ArrayList <String> ();
		ont.add("ontology");
		ont.add("ontologies");
		ont.add("rdf");
		ArrayList <String> rules = new ArrayList <String> ();
		rules.add("rules");
		rules.add("rule language");
		rules.add("rule languages");
		ArrayList <String> reason = new ArrayList <String> ();
		reason.add("inference");
		reason.add("reasoning");
		reason.add("inferencing");
		ArrayList <String> integration = new ArrayList <String> ();
		integration.add("semantic integration");
		
		// counters
		int logicCounter = 0;
		int krCounter = 0;
		int ontCounter = 0;
		int rulesCounter = 0;
		int reasonCounter = 0;
		int integrationCounter = 0;
		
		// Get data from ISWC
		IswcQuery iswcQuery = new IswcQuery ();
		iswcQuery.submitQuery( iswcQuery.getQuery(year) );
		ArrayList <Iswc> iswcData = iswcQuery.getIswcData();
	    System.out.println(" Got " + iswcData.size() + " results for " + year); 
					
		// Loop over all the ISWC abstracts for this year
		for ( int i=0; i<iswcData.size(); i++ ) {
						
		   System.out.println("  Working on Abstract " + (i+1) + " of " + iswcData.size()); 
		   Iswc iswc = iswcData.get(i);
		   String abstrct = iswc.getAbstract();
		   if ( checkForTerms( abstrct, logics ) ) { logicCounter++; }
		   if ( checkForTerms( abstrct, kr ) ) { logicCounter++; }
		   if ( checkForTerms( abstrct, ont ) ) { ontCounter++; }
		   if ( checkForTerms( abstrct, rules ) ) { rulesCounter++; }
		   if ( checkForTerms( abstrct, reason ) ){ reasonCounter++; }
		   if ( checkForTerms( abstrct, integration ) ) { integrationCounter++; }
		   
		} // end for
		
		System.out.println("Logics: " + logicCounter);
		System.out.println("Knowledge Representation: " + krCounter);
		System.out.println("Ontologies: " + ontCounter);
		System.out.println("Rules: " + rulesCounter);
		System.out.println("Resoning: " + reasonCounter);
		System.out.println("Semantic Integration: " + integrationCounter);
		
	}
	
}