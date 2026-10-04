public static Locale getLocale(User u) {
    Locale locale = getLocale(u.getLanguageId());
    try {
        Locale.Builder builder = new Locale.Builder().setLanguage(locale.getLanguage());
        if (u.getAddress() != null && u.getAddress().getCountry() != null) {
            builder.setRegion(u.getAddress().getCountry());
        }
        locale = builder.build();
    } catch (Exception e) {
        log.error("Unexpected Error while constructing locale for the user", e);
    }
    return locale;
}