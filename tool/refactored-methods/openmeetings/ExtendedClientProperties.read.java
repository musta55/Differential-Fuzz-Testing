@Override
public void read(IRequestParameters parameters) {
    super.read(parameters);
    String url = parameters.getParameterValue("codebase").toString(OpenmeetingsVariables.getBaseUrl());
    String cleanedUrl = cleanUrl(url);
    baseUrl = ensureTrailingSlash(cleanedUrl);
    codebase = cleanedUrl + "screenshare";
    settings = parameters.getParameterValue("settings").toString("{}");
}
// ---- helper method(s) introduced by the refactoring ----
private static String removeSemicolon(String url) {
    int semi = url.indexOf(';');
    return semi > -1 ? url.substring(0, semi) : url;
}

private static String removeTail(String url) {
    for (String tail : new String[] { HASH_MAPPING, SIGNIN_MAPPING, NOTINIT_MAPPING }) {
        if (url.endsWith(tail)) {
            return url.substring(0, url.length() - tail.length());
        }
    }
    return url;
}

private static String ensureTrailingSlash(String url) {
    return url.charAt(url.length() - 1) != '/' ? url + '/' : url;
}

