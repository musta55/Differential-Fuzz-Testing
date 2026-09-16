/**
 * @param userAgent User Agent
 * @return version null if not IE or the version after MSIE
 */
protected Float extractIEVersion(String userAgent) {
    if (StringUtils.isEmpty(userAgent)) {
        log.info("userAgent is null");
        return null;
    }
    Matcher matcher = IE_UA_PATTERN.matcher(userAgent);
    if (!matcher.find()) {
        return null;
    }
    String ieVersion = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
    return StringUtils.isNotEmpty(ieVersion) ? Float.valueOf(ieVersion) : null;
}