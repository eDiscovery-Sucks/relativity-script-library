import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
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
            // Load and check schema file
            File schemaFile = new File(xsdPath);
            if (!schemaFile.exists()) {
                System.err.println("Schema file not found at " + schemaFile.getAbsolutePath());
                System.exit(1);
            } else {
                System.out.println("Loaded schema file from: " + schemaFile.getAbsolutePath());
            }

            // Create Schema and Validator
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();

            // Set up SAXParserFactory with strict validation features
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            saxParserFactory.setValidating(false); // Schema validation only

            // Enable strict features on SAXParserFactory
            saxParserFactory.setFeature("http://apache.org/xml/features/validation/schema", true);
            saxParserFactory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            saxParserFactory.setFeature("http://apache.org/xml/features/validation/id-idref-checking", true);
            saxParserFactory.setFeature("http://apache.org/xml/features/validation/identity-constraint-checking", true);

            // Create XMLReader with the configured SAXParserFactory
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            // Set custom error handler to capture validation messages
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

            // Validate using SAXSource with the XMLReader
            SAXSource source = new SAXSource(xmlReader, new org.xml.sax.InputSource(new File(xmlPath).toURI().toString()));
            validator.validate(source);

            // Check for any errors
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
