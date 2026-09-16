public static Date parseImportDate(String dateString) {
    try {
        Date resultDate = validDate(dateFormat__ddMMyyyyHHmmss, dateString);
        if (resultDate != null) {
            return resultDate;
        }
        resultDate = validDate(dateFormat__ddMMyyyy, dateString);
        if (resultDate != null) {
            return resultDate;
        }
        resultDate = validDate(dateString);
        if (dateString != null) {
            return resultDate;
        }
        log.error("parseImportDate:: Could not parse date string {}", dateString);
    } catch (Exception e) {
        log.error("parseImportDate", e);
    }
    return null;
}