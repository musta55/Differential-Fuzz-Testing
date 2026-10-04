public static String validateCountry(String inCode) {
    Set<String> countries = getCountriesSet();
    String code = inCode == null ? "" : inCode.toUpperCase(Locale.ROOT);
    if (!countries.contains(code)) {
        String newCountry = getCountries().get(0);
        log.warn("Invalid country found: {}, will be replaced with: {}", code, newCountry);
        code = newCountry;
    }
    return code;
}
// ---- helper method(s) introduced by the refactoring ----
private static Set<String> getCountriesSet() {
    return new HashSet<>(getCountries());
}

