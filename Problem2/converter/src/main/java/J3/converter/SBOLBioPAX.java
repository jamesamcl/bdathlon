package J3.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;

/** SBOLBioPax - A converter for SBOL <--> BioPAX
 * 
 * @author Jonny Naylor - BDAthlon - 16/08/16
 *
 */
public class SBOLBioPAX {
	
	/** Constructor **/
	public SBOLBioPAX(){
		
	}
	
	public void convertToSBOL(String biopax_file, String sbol_file) throws IOException, SBOLValidationException, SBOLConversionException {
		File sbol_file_obj = File.createTempFile(sbol_file, ".sbol");

		InputStream input_stream = getClass().getResourceAsStream(biopax_file);

		SBOLBiopaxConverter converter = new SBOLBiopaxConverter();	
		converter.convertToSBOL(input_stream, sbol_file);
	}
	
}
