private static void setSchemaResult(AssertionResult result, String xmlStr, String xsdFileName) {
    try {
        DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();
        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);
        parserFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        parserFactory.setAttribute(JAXP_SCHEMA_SOURCE, xsdFileName);
        parserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        // create a parser:
        DocumentBuilder parser = parserFactory.newDocumentBuilder();
        parser.setErrorHandler(new SAXErrorHandler(result));
        parser.parse(new InputSource(new StringReader(xmlStr)));
        // if everything went fine then xml schema validation is valid
    } catch (SAXParseException e) {
        // Only set message if error not yet flagged
        if (!result.isError() && !result.isFailure()) {
            result.setError(true);
            result.setFailureMessage(errorDetails(e));
        }
    } catch (SAXException e) {
        if (log.isWarnEnabled()) {
            log.warn(e.toString());
        }
        result.setResultForFailure(e.getMessage());
    } catch (IOException e) {
        log.warn("IO error", e);
        result.setResultForFailure(e.getMessage());
    } catch (ParserConfigurationException e) {
        log.warn("Problem with Parser Config", e);
        result.setResultForFailure(e.getMessage());
    }
}