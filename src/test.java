package narock;

import java.io.File;

import org.apache.commons.codec.Decoder;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparator.TermOrdValComparator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;


public class test {

	// Semantic Web Conference Corpus
	public static final String iswc = "http://data.semanticweb.org/sparql";
	
	// DBpedia public sparql endpoint
	public static final String dbpedia = "http://DBpedia.org/sparql";
	
	// AGU Rackspace SPARQL Endpoint for abstracts
	public static final String agu = "http://198.61.161.98:8890/sparql";
	
	public static void main(String[] args) throws Exception {
		//testEndpoint(iswc);
		//testQuery(dbpedia);
		
		String sparqlDBPedia = "SELECT ?abstract WHERE { { <http://dbpedia.org/resource/Civil_engineering> <http://dbpedia.org/ontology/abstract> ?abstract FILTER langMatches( lang(?abstract), ‘en’) }}";
		String sparqlISWCTest = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT ?label WHERE {<http://dbpedia.org/resource/Asturias> rdfs:label ?label .}";
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
			   " SELECT DISTINCT ?s ?a " +
			   "	WHERE { " +
			   "		?s swc:isPartOf " + "<http://data.semanticweb.org/conference/iswc/" + "2009" + "/proceedings>" + " . " +
			   "	 	?s swrc:abstract ?a . " +
			   "		?s foaf:maker ?au  " +
			   "    } ";
   	 
   	// System.out.print(sparqlQueryString);

   	//testISWC(iswc, sparqlISWCTest);
	//	testISWC(iswc, sparqlQueryString);
		
		HighFreqTerms hfr = new HighFreqTerms();
		String[] arg = new String[2];
		arg[0] = "C:\\temp1";
		arg[1] = "-t";
		//hfr.main(arg);
		

	
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(arg[0])));
		
		System.out.println(reader.numDocs());
		String fields[] = new String[1];
		fields[0]="contents";
//
		TermStats[] topTerms = HighFreqTerms.getHighFreqTerms(reader, 25, fields);
	    StringDecoder dec = new StringDecoder(); 
				//decoders.get(topTerms[i].field);
		
		for(int i = 0; i<topTerms.length; i++)
		{
			System.out.println("Field:" + topTerms[i].field + " term:"+ topTerms[i].termtext.utf8ToString() +  " DocFreq:" + topTerms[i].docFreq);
			String s = " holla" + dec.decodeTerm(topTerms[i].field, topTerms[i].termtext.utf8ToString());
			//System.out.println("top terms" + topTerms[i].toString());
		}		
	
	}
	protected ResultSet queryEndpoint ( String endpoint, String sparqlQueryString ) 
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

	public static void testISWC(String endpoint, String sparql)
	{
		String queryASK = sparql;
		Query query = QueryFactory.create(queryASK);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results);

		qexec.close();	
	}

	public static void testQuery (String endpoint) {
		String queryASK = "SELECT ?abstract "
						+ "WHERE { { <http://dbpedia.org/resource/Brooklyn> <http://dbpedia.org/ontology/abstract> ?abstract filter(langMatches(lang(?abstract),'en'))  } }";
		Query query = QueryFactory.create(queryASK);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
		
		ResultSet results = qexec.execSelect();
	    //ResultSetFormatter.out(System.out, results);
	    
	    qexec.close();
	}
	
	
}
