public static Date parseDate(String dateString) {
    try {
        return dateFormat__ddMMyyyy.parse(dateString);
    } catch (Exception e) {
        log.error("parseDate", e);
    }
    return null;
}