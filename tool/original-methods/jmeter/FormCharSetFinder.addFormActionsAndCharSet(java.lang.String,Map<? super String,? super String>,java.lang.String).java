/**
 * Add form action urls and their corresponding encodings for all forms on the page
 *
 * @param html the html to parse for form encodings
 * @param formEncodings the Map where form encodings should be added
 * @param pageEncoding the encoding used for the whole page
 * @throws HTMLParseException when parsing the <code>html</code> fails
 */
public void addFormActionsAndCharSet(String html, Map<? super String, ? super String> formEncodings, String pageEncoding) throws HTMLParseException {
    log.debug("Parsing html of: {}", html);
    Document document;
    try {
        document = Jsoup.parse(html);
    } catch (RuntimeException e) {
        throw new HTMLParseException("Could not parse HTML to look for forms charsets", e);
    }
    Elements forms = document.select("form");
    for (Element element : forms) {
        String action = element.attr("action");
        if (!StringUtils.isEmpty(action)) {
            // We use the page encoding where the form resides, as the
            // default encoding for the form
            String formCharSet = pageEncoding;
            String acceptCharSet = element.attr("accept-charset");
            // Check if we found an accept-charset attribute on the form
            if (acceptCharSet != null) {
                String[] charSets = JOrphanUtils.split(acceptCharSet, ",");
                // Just use the first one of the possible many charsets
                if (charSets.length > 0) {
                    formCharSet = charSets[0].trim();
                    if (formCharSet.isEmpty()) {
                        formCharSet = null;
                    }
                }
            }
            if (formCharSet != null) {
                formEncodings.put(action, formCharSet);
            }
        }
    }
}