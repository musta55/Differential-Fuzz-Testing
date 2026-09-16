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
    String ieVersion = null;
    if (matcher.find()) {
        if (matcher.groupCount() > 0) {
            ieVersion = matcher.group(1);
        } else {
            ieVersion = matcher.group();
        }
    }
    if (ieVersion != null) {
        return Float.valueOf(ieVersion);
    } else {
        return null;
    }
}