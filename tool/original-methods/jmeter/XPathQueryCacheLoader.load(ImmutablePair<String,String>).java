@Override
public XPathExecutable load(ImmutablePair<String, String> key) throws Exception {
    String xPathQuery = key.left;
    String namespacesString = key.right;
    Processor processor = XPathUtil.getProcessor();
    XPathCompiler xPathCompiler = processor.newXPathCompiler();
    List<String[]> namespacesList = XPathUtil.namespacesParse(namespacesString);
    log.debug("Parsed namespaces:{} into list of namespaces:{}", namespacesString, namespacesList);
    for (String[] namespaces : namespacesList) {
        xPathCompiler.declareNamespace(namespaces[0], namespaces[1]);
    }
    log.debug("Declared namespaces:{}, now compiling xPathQuery:{}", namespacesList, xPathQuery);
    return xPathCompiler.compile(xPathQuery);
}