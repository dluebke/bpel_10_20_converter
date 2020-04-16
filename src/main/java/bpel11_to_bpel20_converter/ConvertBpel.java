package bpel11_to_bpel20_converter;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class ConvertBpel {
	private final static Schema schema;

	static {
		SchemaFactory xmlSchemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema s = null;
		try {
			s = xmlSchemaFactory.newSchema(new StreamSource(ConvertBpel.class.getResourceAsStream("/ws-bpel_executable.xsd")));
		} catch (SAXException e) {
			e.printStackTrace();
		}
		schema = s;
	}
	
	public static void main(String[] args) {
		Source xsl = new StreamSource(ConvertBpel.class.getResourceAsStream("/bpel11_to_20.xsl"));
		
		Source xmlInput = new StreamSource(new File(args[0]));
		File outputFile = new File(args[1]);
		Result xmlOutput = new StreamResult(outputFile);
		try {
		    Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
		    transformer.transform(xmlInput, xmlOutput);
		} catch (TransformerException e) {
		    e.printStackTrace();
		    System.exit(1);
		}
		
		try {
			schema.newValidator().validate(new StreamSource(outputFile));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
}
