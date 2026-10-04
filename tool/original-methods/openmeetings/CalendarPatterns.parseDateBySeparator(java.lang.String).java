public static Date parseDateBySeparator(String dateString) {
    try {
        return dateFormat__ddMMyyyyBySeparator.parse(dateString);
    } catch (Exception e) {
        log.error("parseDateBySeparator", e);
    }
    return null;
}