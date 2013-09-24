package sparql;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class Endpoints 
{
	
	// Semantic Web Conference Corpus
	public static String iswc = "http://data.semanticweb.org/sparql";
	
	// DBpedia public sparql endpoint
	public static String dbpedia = "http://DBpedia.org/sparql";
	
	// AGU Rackspace SPARQL Endpoint for abstracts
	public static String agu = "http://198.61.161.98:8890/sparql";
	
	public static ResultSet queryEndpoint ( String endpoint, String sparqlQueryString ) 
    {
    	Query query = QueryFactory.create(sparqlQueryString);
        ARQ.getContext().setTrue(ARQ.useSAX);
    	QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
    	ResultSet results = qexec.execSelect();                                       
    	qexec.close();
    	return results; 
    }
	
	public static void testEndpoint ( String endpoint )
	{
		String queryASK = "ASK { }";
    	Query query = QueryFactory.create(queryASK);
    	QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
        try {
            if (qe.execAsk()) { System.out.println( endpoint + " is UP" ); } 
        }   catch (QueryExceptionHTTP e) {
            System.out.println( endpoint + " is DOWN");
            System.out.println( e );
        } finally { qe.close(); } 
	}
	
}