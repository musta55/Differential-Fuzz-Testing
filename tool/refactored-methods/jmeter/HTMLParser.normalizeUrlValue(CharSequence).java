/**
 * Normalizes URL as browsers do
 * @param url {@link CharSequence}
 * @return normalized url
 */
protected static String normalizeUrlValue(CharSequence url) {
    if (StringUtils.isEmpty(url)) {
        return null;
    }
    String trimmed = NORMALIZE_URL_PATTERN.matcher(url.toString().trim()).replaceAll("");
    return trimmed.isEmpty() ? null : trimmed;
}