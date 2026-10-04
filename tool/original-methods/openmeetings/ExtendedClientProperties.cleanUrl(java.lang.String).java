private static StringBuilder cleanUrl(String inUrl) {
    StringBuilder sb = new StringBuilder();
    String url = inUrl;
    int semi = url.indexOf(';');
    if (semi > -1) {
        url = url.substring(0, semi);
    }
    for (String tail : new String[] { HASH_MAPPING, SIGNIN_MAPPING, NOTINIT_MAPPING }) {
        if (url.endsWith(tail)) {
            url = url.substring(0, url.length() - tail.length());
            break;
        }
    }
    return sb.append(url);
}