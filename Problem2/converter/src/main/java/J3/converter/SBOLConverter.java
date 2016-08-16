package J3.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;

public interface SBOLConverter {
	
	/**
	 * Convert inputstream in owl/rdf format to outputstream in JSON format.
	 * 
	 * @param in input stream (BioPAX RDF/XML data)
	 * @param os output stream (to write the SBOL result)
	 * @throws IOException when an I/O error occurs
	 */	
	public void convertToSBOL(InputStream in, String sbol_file) throws IOException, SBOLValidationException, SBOLConversionException;
	
	/**
	 * Convert inputstream in SBOL format to outputsream in owl/rdf format.
	 * 
	 * @param in input stream (SBOL data)
	 * @param out output stream (to write the BioPAX RDF/XML result)
	 */	
	 public void convertFromSBOL(InputStream in, OutputStream out);
	
}
