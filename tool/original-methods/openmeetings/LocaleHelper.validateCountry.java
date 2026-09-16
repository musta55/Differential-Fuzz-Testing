public static String validateCountry(String inCode) {
    List<String> list = getCountries();
    Set<String> countries = new HashSet<>(list);
    String code = inCode == null ? "" : inCode.toUpperCase(Locale.ROOT);
    if (!countries.contains(code)) {
        String newCountry = list.get(0);
        log.warn("Invalid country found: {}, will be replaced with: {}", code, newCountry);
        code = newCountry;
    }
    return code;
}