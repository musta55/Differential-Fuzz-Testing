private static Date validDate(String testdate) {
    try {
        Long t = Long.valueOf(testdate);
        if (t != null) {
            return new Date(t);
        }
    } catch (Exception err) {
        //no-op
    }
    return null;
}