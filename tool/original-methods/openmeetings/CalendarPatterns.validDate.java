private static Date validDate(FastDateFormat sdf, String testdate) {
    Date resultDate = null;
    try {
        resultDate = sdf.parse(testdate);
    } catch (ParseException | NumberFormatException e) {
        // if the format of the string provided doesn't match the format we
        // declared in SimpleDateFormat() we will get an exception
        return null;
    }
    if (!sdf.format(resultDate).equals(testdate)) {
        return null;
    }
    return resultDate;
}