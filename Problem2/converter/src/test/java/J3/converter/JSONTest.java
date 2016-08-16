package J3.converter;

import org.biopax.paxtools.io.jsonld.JsonldBiopaxConverter;
import org.biopax.paxtools.io.jsonld.JsonldConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class JSONTest {

	public static void main(String[] args){
		JSONTest json_test = new JSONTest();
		
		try {
			json_test.test();
		}catch(Exception e){
			System.out.println("JSONTest() : Error when running test()");
			e.printStackTrace();
		}
	}
	
	public JSONTest(){
		
	}
	
	public void test() throws IOException{
		
		File jsonldTestFileName = File.createTempFile("test", ".jsonld");
		File rdfTestFileName = File.createTempFile("test", ".rdf");

		JsonldConverter intf = new JsonldBiopaxConverter();
		
		// convert owl test file in resource directory to jsonld format
		InputStream in = getClass().getResourceAsStream("PC2v5test-Signaling-By-BMP-Pathway-REACT_12034.2.owl");
		intf.convertToJsonld(in, new FileOutputStream(jsonldTestFileName));

		// convert jsonld test file back to rdf format
		InputStream inputLD = new FileInputStream(jsonldTestFileName);
		OutputStream outRDF = new FileOutputStream(rdfTestFileName);
		intf.convertFromJsonld(inputLD, outRDF);
	}
}
