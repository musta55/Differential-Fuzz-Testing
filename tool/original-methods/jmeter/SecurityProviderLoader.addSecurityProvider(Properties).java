public static void addSecurityProvider(Properties properties) {
    properties.keySet().stream().filter(key -> key.toString().matches("security\\.provider(\\.\\d+)?")).sorted(Comparator.comparing(String::valueOf)).forEach(key -> addSecurityProvider(properties.get(key).toString()));
}