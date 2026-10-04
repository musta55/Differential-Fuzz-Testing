/**
 * Returns the result of the Assertion.
 * Here it checks whether the Sample data is XML.
 * If so an AssertionResult containing a FailureMessage will be returned.
 * Otherwise the returned AssertionResult will reflect the success of the Sample.
 */
@Override
public AssertionResult getResult(SampleResult response) {
    // no error as default
    AssertionResult result = new AssertionResult(getName());
    String resultData = response.getResponseDataAsString();
    if (resultData.length() == 0) {
        return result.setResultForNull();
    }
    result.setFailure(false);
    XMLReader builder = XML_READER.get();
    if (builder != null) {
        try {
            builder.setErrorHandler(new LogErrorHandler());
            parseXML(builder, resultData);
        } catch (SAXException | IOException e) {
            result.setError(true);
            result.setFailure(true);
            result.setFailureMessage(e.getMessage());
        }
    } else {
        result.setError(true);
        result.setFailureMessage("Cannot initialize XMLReader in element:" + getName() + ", check jmeter.log file");
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Creates and configures an XMLReader instance.
 */
private static XMLReader createXMLReader() {
    try {
        XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return reader;
    } catch (SAXException | ParserConfigurationException e) {
        log.error("Error initializing XMLReader in XMLAssertion", e);
        return null;
    }
}

/**
 * Parses the XML content using the provided XMLReader.
 */
private static void parseXML(XMLReader builder, String xmlContent) throws SAXException, IOException {
    builder.parse(new InputSource(new StringReader(xmlContent)));
}

