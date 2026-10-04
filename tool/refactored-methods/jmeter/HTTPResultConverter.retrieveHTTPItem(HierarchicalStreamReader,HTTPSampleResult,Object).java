private static void retrieveHTTPItem(HierarchicalStreamReader reader, HTTPSampleResult res, Object subItem) {
    if (subItem instanceof URL) {
        res.setURL((URL) subItem);
    } else {
        String nodeName = reader.getNodeName();
        switch(nodeName) {
            case TAG_COOKIES:
                res.setCookies((String) subItem);
                break;
            case TAG_METHOD:
                res.setHTTPMethod((String) subItem);
                break;
            case TAG_QUERY_STRING:
                res.setQueryString((String) subItem);
                break;
            case TAG_REDIRECT_LOCATION:
                res.setRedirectLocation((String) subItem);
                break;
            default:
                // Default case to handle unexpected node names
                break;
        }
    }
}