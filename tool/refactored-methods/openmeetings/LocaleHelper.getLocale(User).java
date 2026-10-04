public static Locale getLocale(User u) {
    Locale locale = getLocale(u.getLanguageId());
    try {
        Locale.Builder builder = new Locale.Builder().setLanguage(locale.getLanguage());
        if (u.getAddress() != null && u.getAddress().getCountry() != null) {
            builder.setRegion(u.getAddress().getCountry());
        }
        locale = builder.build();
    } catch (IllegalArgumentException e) {
        log.error("Error while constructing locale for the user due to invalid region", e);
    } catch (Exception e) {
        log.error("Unexpected error while constructing locale for the user", e);
    }
    return locale;
}
// ---- helper method(s) introduced by the refactoring ----
private static Set<String> getCountriesSet() {
    return new HashSet<>(getCountries());
}

