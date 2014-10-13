package narock;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

import java.io.*;
import java.util.ArrayList;


public class WimmDex {
	  private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	  private final static String location = "C:\\New folder\\federalist-papers\\texts";
	  private static String searchTerm = "government";
	  private final static int NUM_OF_DOCS = 25;
	  

	  private IndexWriter writer;
	  private ArrayList<File> queue = new ArrayList<File>();


	  public static void main(String[] args) throws IOException, Exception {
	   
	    String indexLocation = location;

	    TextFileIndexer indexer = null;
	    //create the indexer
	    try {
	      indexer = new TextFileIndexer(indexLocation);
	    } catch (Exception ex) {
	      System.out.println("Cannot create index..." + ex.getMessage());
	      System.exit(-1);
	    }

	    //index it and close the index
	    indexer.indexFileOrDirectory(indexLocation);
	    indexer.closeIndex();

	    //search for a term
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(NUM_OF_DOCS, true);
	   
	    
		Term t = new Term(searchTerm);
	  
		System.out.println( reader.totalTermFreq(t));
		System.out.println( reader.docFreq(t));
		
        Query q = new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(searchTerm);
        searcher.search(q, collector);
        //ScoreDoc[] hits = collector.topDocs().scoreDocs;
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
        }
	  }
	   
	  /**
	   * Constructor
	   * @param indexDir the name of the folder in which the index should be created
	   * @throws java.io.IOException when exception creating index.
	   */
	  WimmDex(String indexDir) throws IOException {
	    FSDirectory dir = FSDirectory.open(new File(indexDir));
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	    writer = new IndexWriter(dir, config);
	  }


	  public void indexFileOrDirectory(String fileName) throws IOException {

	    addFiles(new File(fileName));
	    
	    int originalNumDocs = writer.numDocs();
	    for (File f : queue) {
	      FileReader fr = null;
	      try {
	        Document doc = new Document();

	        //===================================================
	        // add contents of file
	        //===================================================
	        fr = new FileReader(f);
	        doc.add(new TextField("contents", fr));
	        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
	        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

	        writer.addDocument(doc);
	        System.out.println("Added: " + f);
	      } catch (Exception e) {
	        System.out.println("Could not add: " + f);
	      } finally {
	        fr.close();
	      }
	    }
	    
	    int newNumDocs = writer.numDocs();
	    System.out.println("");
	    System.out.println("************************");
	    System.out.println((newNumDocs - originalNumDocs) + " documents added.");
	    System.out.println("************************");

	    queue.clear();
	  }

	  private void addFiles(File file) {

	    if (!file.exists()) {
	      System.out.println(file + " does not exist.");
	    }
	    if (file.isDirectory()) {
	      for (File f : file.listFiles()) {
	        addFiles(f);
	      }
	    } else {
	      String filename = file.getName().toLowerCase();
	      //===================================================
	      // Only index text files
	      //===================================================
	      if (filename.endsWith(".htm") || filename.endsWith(".html") || 
	              filename.endsWith(".xml") || filename.endsWith(".txt")) {
	        queue.add(file);
	      } else {
	        System.out.println("Skipped " + filename);
	      }
	    }
	  }

	  /**
	   * Close the index.
	   * @throws java.io.IOException when exception closing
	   */
	  public void closeIndex() throws IOException {
	    writer.close();
	  }
	  
	  public void TFIDF(){
		  DefaultSimilarity df = new DefaultSimilarity();
		  try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("indexLocation")));
//			TermsEnum te = reader.docFreq("federalist");
			Term t = new Term("federalist");
			
			double x = reader.docFreq(t);
			
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	  }
	}