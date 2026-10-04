/**
 * Normalizes URL as browsers do
 * @param url {@link CharSequence}
 * @return normalized url
 */
protected static String normalizeUrlValue(CharSequence url) {
    if (!StringUtils.isEmpty(url)) {
        String trimmed = NORMALIZE_URL_PATTERN.matcher(url.toString().trim()).replaceAll("");
        if (!trimmed.isEmpty()) {
            return trimmed;
        }
    }
    return null;
}