import org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

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

        AtomicBoolean hasErrors = new AtomicBoolean(false);
        
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            System.out.println("Loaded schema: " + xsdPath);

            factory.setFeature("http://apache.org/xml/features/validation/schema", true);
            factory.setFeature("http://xml.org/sax/features/validation", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            factory.setFeature("http://apache.org/xml/features/validation/id-idref-checking", true);
            factory.setFeature("http://apache.org/xml/features/validation/identity-constraint-checking", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema/normalized-value", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema/element-default", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
            factory.setFeature("http://apache.org/xml/features/standard-uri-conformant", true);
            factory.setFeature("http://xml.org/sax/features/namespaces", true);
            factory.setFeature("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", true);
            factory.setFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", true);
            factory.setFeature("http://apache.org/xml/features/warn-on-duplicate-entitydef", true);
            
            Validator validator = schema.newValidator();

            validator.setErrorHandler(new org.xml.sax.ErrorHandler() {
                public void warning(SAXParseException e) {
                    System.err.println("Warning: " + e.getMessage());
                    e.printStackTrace(System.err);
                    hasErrors.set(true);
                }

                public void error(SAXParseException e) {
                    System.err.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
                    e.printStackTrace(System.err);
                    hasErrors.set(true);
                }

                public void fatalError(SAXParseException e) {
                    System.err.println(ANSI_RED + "Fatal error: " + e.getMessage() + ANSI_RESET);
                    e.printStackTrace(System.err);
                    hasErrors.set(true);
                }
            });
            
            validator.validate(new StreamSource(new File(xmlPath)));

            if (hasErrors.get()) {
                System.err.println(ANSI_RED + "Validation failed due to errors." + ANSI_RESET);
                System.exit(1);
            }
            
            System.out.println("Validation successful.");
        } catch (SAXException e) {
            System.err.println(ANSI_RED + "Validation failed: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (Exception e) {
            System.err.println(ANSI_RED + "An error occurred: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
