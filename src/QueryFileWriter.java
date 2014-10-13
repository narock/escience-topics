package narock;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;


public class QueryFileWriter {

	// Semantic Web Conference Corpus
	public static final String iswc = "http://data.semanticweb.org/sparql";
	
	// DBpedia public sparql endpoint
	public static final String dbpedia = "http://DBpedia.org/sparql";
	
	// AGU Rackspace SPARQL Endpoint for abstracts
	public static final String agu = "http://198.61.161.98:8890/sparql";
	
	public static void main(String[] args) throws IOException {
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
   	 
   	 System.out.print(sparqlQueryString);

   	 writeISWC(iswc, sparqlQueryString);
	}


/*	public static void testISWC(String endpoint, String sparql)//Console Query Result -- Debugging
	{
		String queryASK = sparql;
		Query query = QueryFactory.create(queryASK);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results);

		qexec.close();	
	}*/
	
	public static void writeISWC(String endpoint, String sparql) throws IOException
	{
		boolean append_to_file = false;//Does not add to file / Clears contents of file and replaces with input
		
		String queryASK = sparql;
		Query query = QueryFactory.create(queryASK);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		ResultSet results = qexec.execSelect();
		String resultString = ResultSetFormatter.asText(results);
		writeToFile(resultString, "resultcontents.txt", append_to_file);// debugging - prints query results to text file each run
		Scanner scanner = new Scanner(resultString);
		
		int line = 0;
		while(scanner.hasNextLine()) {
			line ++;
			String resultLine = scanner.nextLine();
			if (line >= 4) {
				String firstChar = resultLine.substring(1,2);
				if ((!firstChar.equals("-"))) {
					String url = resultLine.substring(resultLine.indexOf("<") + 1, resultLine.indexOf(">"));
					String desc = resultLine.substring(resultLine.indexOf("\"") + 1, resultLine.indexOf("\"", resultLine.indexOf("\"") + 1));;
					String path = "results/";
					
					String filenameArr[] = url.split("/");
					for (int i = 5; i < filenameArr.length; i++) {
						path = path + filenameArr[i];
						if (i != filenameArr.length -1 ) {
							path += "-";
						} else {
							path += ".txt";
						}
					}
					writeToFile(desc, path, true);//creates text files named by source location, filled with descriptions
				}
			}
		}
		scanner.close();	
	}
	
	public static String parseLine(ResultSet results){
		String path = "";
		String resultset = ResultSetFormatter.asText(results);
		System.out.println(resultset);
		return path;
	}
	
	public static void writeToFile( String textLine, String path, boolean append_to_file ) throws IOException {
		FileWriter write = new FileWriter( path , append_to_file);
		PrintWriter print_line = new PrintWriter( write );
		print_line.printf( "%s" + "%n" , textLine);
		print_line.close();
	}

}
