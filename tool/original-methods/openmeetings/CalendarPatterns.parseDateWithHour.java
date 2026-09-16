public static Date parseDateWithHour(String dateString) {
    try {
        if (dateString == null || dateString.length() == 0 || "null".equals(dateString)) {
            return null;
        }
        return dateFormat__ddMMyyyyHHmmss.parse(dateString);
    } catch (Exception e) {
        log.error("parseDateWithHour", e);
    }
    return null;
}