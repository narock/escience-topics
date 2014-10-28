package narock;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.BytesRef;

public class StringDecoder implements Decoder {
  //BinaryDecoder b = new BinaryDecoder();

  public String decodeTerm(String fieldName, Object value) {
    if (value == null) {
      return "(null)";
    } else if (value instanceof BytesRef) {
      return ((BytesRef)value).utf8ToString();
    } else {
      return value.toString();
    }
  }
  
  public String decodeStored(String fieldName, Field value) throws Exception {
    if (value.binaryValue() != null) {
      //return b.decodeStored(fieldName, value);
    	return "-1";
    }
    String val = value.stringValue();
    if (val == null && value.numericValue() != null) {
      val = value.numericValue().toString();
    }
    return decodeTerm(fieldName, val);
  }

  public String toString() {
    return "string utf8";
  }

@Override
public Object decode(Object arg0) throws DecoderException {
	// TODO Auto-generated method stub
	return null;
}
}