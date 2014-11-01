package narock;

import java.io.IOException;

import org.apache.lucene.index.Terms;
import org.apache.lucene.util.AttributeSource;

/** Enumerates indexed fields.  You must first call {@link
 *  #next} before calling {@link #terms}.
 *
 * @lucene.experimental */

public  class FieldsEnum1 {

  // TODO: maybe allow retrieving FieldInfo for current
  // field, as optional method?

  private AttributeSource atts = null;

  /**
   * Returns the related attributes.
   */
  public AttributeSource attributes() {
    if (atts == null) {
      atts = new AttributeSource();
    }
    return atts;
  }
  
  /** Increments the enumeration to the next field. Returns
   * null when there are no more fields.*/
  public  String next() throws IOException {
	return null;
}

  // TODO: would be nice to require/fix all impls so they
  // never return null here... we have to fix the writers to
  // never write 0-terms fields... or maybe allow a non-null
  // Terms instance in just this case

  /** Get {@link Terms} for the current field.  After {@link #next} returns
   *  null this method should not be called. This method may
   *  return null in some cases, which means the provided
   *  field does not have any terms. */
  public Terms terms() throws IOException {
	return null;
}

  // TODO: should we allow pulling Terms as well?  not just
  // the iterator?
  
  public final static FieldsEnum1[] EMPTY_ARRAY = new FieldsEnum1[0];

  /** Provides zero fields */
  public final static FieldsEnum1 EMPTY = new FieldsEnum1() {

    @Override
    public String next() {
      return null;
    }

    @Override
    public Terms terms() {
      throw new IllegalStateException("this method should never be called");
    }
  };
}

