package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import sparql.*;

// This is simple test query of Dbpedia. Dbpedia is not used in this study.
// This code is simply a sanity check of SPARQL endpoint querying

public class DbpediaQuery extends Endpoints
{
		 
     public static void main(String[] args) {

    	 DbpediaQuery endpoint = new DbpediaQuery ();
    	 endpoint.testEndpoint( endpoint.dbpedia  );
    	 ResultSet results = endpoint.submitQuery( );
    	 while (results.hasNext())
    	 {
    		 QuerySolution soln = results.nextSolution();
             System.out.println(soln.get("?abstract"));  
    	 }
	 
     }

	 public ResultSet submitQuery( ) {
	        
		 String sparqlQueryString = 
				   " SELECT ?abstract " +
                   " WHERE {{ " +
                   "   <http://dbpedia.org/resource/Mars> " +
                   "      <http://dbpedia.org/ontology/abstract> " +
                   "          ?abstract }}";
		 ResultSet results = queryEndpoint( this.dbpedia, sparqlQueryString );
		 return results;

	 }

}