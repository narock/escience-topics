package sparql.query;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import sparql.*;

public class DbpediaQuery 
{
	
	 private String endpoint = Endpoints.dbpedia;
	 
     public static void main(String[] args) {

    	 DbpediaQuery dbpedia = new DbpediaQuery ();
    	 dbpedia.testEndpoint( );
    	 ResultSet results = dbpedia.submitQuery( );
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
		 ResultSet results = Endpoints.queryEndpoint( endpoint, sparqlQueryString );
		 return results;

	 }

	 public void testEndpoint() { Endpoints.testEndpoint( endpoint ); }

}