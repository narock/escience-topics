package sparql;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public abstract class Endpoints 
{
	
	// Semantic Web Conference Corpus
	public final String iswc = "http://data.semanticweb.org/sparql";
	
	// DBpedia public sparql endpoint
	public final String dbpedia = "http://DBpedia.org/sparql";
	
	// AGU Rackspace SPARQL Endpoint for abstracts
	public final String agu = "http://198.61.161.98:8890/sparql";
	
	protected ResultSet queryEndpoint ( String endpoint, String sparqlQueryString ) 
    {
    	Query query = QueryFactory.create(sparqlQueryString);
        ARQ.getContext().setTrue(ARQ.useSAX);
    	QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
    	ResultSet results = qexec.execSelect();                                       
    	qexec.close();
    	return results; 
    }
	
	public void testEndpoint ( String endpoint )
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