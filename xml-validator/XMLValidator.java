import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants
import java.io.File;

public class XMLValidator {
    public static final String ANSI_RED = "\033[0;31m";
    public static final String ANSI_RESET = "\033[0m";
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java XMLValidator <schema.xsd> <file.xml>");
            return;
        }

        String xsdPath = args[0];
        String xmlPath = args[1];

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            System.out.println("Loaded schema: " + xsdPath);
            
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            System.out.println("Validation successful.");
        } catch (SAXException e) {
            System.out.println(ANSI_RED + "Validation failed: " + e.getMessage() + ANSI_RESET);
            System.exit(1);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "An error occurred: " + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }
    }
}
