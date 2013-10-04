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
        ConfigurationParser parser = new ConfigurationParser();
        xr.setContentHandler(parser);
        xr.setErrorHandler(parser);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        return parser.config;
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
  
	  if ( name.equals("databaseURL") ) this.config.setDatabaseURL(d);
	  if ( name.equals("databaseUsername") ) this.config.setDatabaseUsername(d);
	  if ( name.equals("databasePassword") ) this.config.setDatabasePassword(d);
	  if ( name.equals("nlpModelPath") ) this.config.setNLPmodelPath(d);
	  if ( name.equals("verbose") ) this.config.setVerbose( Boolean.valueOf(d) );
	  if ( name.equals("minSupport") ) this.config.setMinSupport( Double.valueOf(d) );
	  if ( name.equals("year") ) this.config.setYear(d);
	  if ( name.equals("dbOccurrenceTable") ) this.config.setDbCooccurrenceTable(d);
	  
    }
    
     /** Method for parsing the character data of the XML file
	  *
	  */
    public void characters (char ch[], int start, int length) {
       accumulator.append(ch, start, length);
    }

}
