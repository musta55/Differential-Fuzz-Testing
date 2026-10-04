private static void retrieveHTTPItem(HierarchicalStreamReader reader, HTTPSampleResult res, Object subItem) {
    if (subItem instanceof URL) {
        res.setURL((URL) subItem);
    } else {
        String nodeName = reader.getNodeName();
        if (nodeName.equals(TAG_COOKIES)) {
            res.setCookies((String) subItem);
        } else if (nodeName.equals(TAG_METHOD)) {
            res.setHTTPMethod((String) subItem);
        } else if (nodeName.equals(TAG_QUERY_STRING)) {
            res.setQueryString((String) subItem);
        } else if (nodeName.equals(TAG_REDIRECT_LOCATION)) {
            res.setRedirectLocation((String) subItem);
        }
    }
}