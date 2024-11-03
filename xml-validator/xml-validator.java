import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class XMLValidator {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java XMLValidator <schema.xsd> <file.xml>");
            return;
        }

        String xsdPath = args[0];
        String xmlPath = args[1];

        try {
            SchemaFactory factory = new XMLSchemaFactory();
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(new File(xmlPath)));
            System.out.println("Validation successful.");
        } catch (SAXException e) {
            System.out.println("Validation failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
