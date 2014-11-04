package util;


import java.io.FileReader;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


/** An XML Parser for the configuration file 
 * 
 * @author Tom Narock
 * 
 */
public class ConfigurationParser extends DefaultHandler {
 
	Configuration config = new Configuration ();
    StringBuffer accumulator = new StringBuffer();
    
     /** Parse the config file and return the information in a Configuration object
      * 
	  * @param  file  the file to parse
	  * @return Configuration configuration object
	  */
    public Configuration parse ( String file ) throws Exception {
    	XMLReader xr = XMLReaderFactory.createXMLReader();
        ConfigurationParser handler = new ConfigurationParser();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        return handler.config;
    }

    public ConfigurationParser() {
      super();
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////

     /** Method which tells the XML parser what to do at the start of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  */
    public void startElement (String uri, String name,
		      String qName, Attributes atts) {
    	
    	accumulator.setLength(0); // Ready to accumulate new text
    	    	
    }
    
     /** Method which tells the XML parser what to do at the end of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  * @param  qName  the fully qualified name of the element
	  */
    public void endElement (String uri, String name, String qName) {

      String d = accumulator.toString().trim();
  	
  	  if ( name.equals("WordNetPath") ) this.config.wordNetPath = d;
      if ( name.equals("WordNetVersion") ) this.config.wordNetVersion = d;
      if ( name.equals("WordNetCorpus") ) this.config.wordNetCorpus = d;
      if ( name.equals("WordNetDictPath") ) this.config.wordNetDictPath = d;
      if ( name.equals("OntFile") ) this.config.ontFile = d;
      if ( name.equals("MpqaPolarityWordsFile") ) this.config.polarityWordsFile = d;
      if ( name.equals("PositivePolarityWordsFile") ) this.config.positivePolarityWordsFile = d;
      if ( name.equals("NegativePolarityWordsFile") ) this.config.negativePolarityWordsFile = d;
	  if ( name.equals("OutputFile") ) this.config.outputFile = d;
	  if ( name.equals("SentenceDetector") ) this.config.sentence = d;
	  if ( name.equals("Tokenizer") ) this.config.tokenizer = d;
	  if ( name.equals("POS") ) this.config.pos = d;
	   
    }
    
     /** Method for parsing the character data of the XML file
	  *
	  */
    public void characters (char ch[], int start, int length) {
       accumulator.append(ch, start, length);
    }

}
