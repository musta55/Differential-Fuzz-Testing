public static void addSecurityProvider(String securityProviderConfig) {
    Matcher matcher = CONFIGURATION_REGEX.matcher(securityProviderConfig);
    if (matcher.matches()) {
        final String classname = matcher.group("classname");
        final int position = Integer.parseInt(Objects.toString(matcher.group("position"), "0"));
        final String config = matcher.group("config");
        try {
            @SuppressWarnings("unchecked")
            Class<Provider> providerClass = (Class<Provider>) Class.forName(classname);
            Provider provider = null;
            if (config != null) {
                provider = tryConstructorWithString(providerClass, config);
            }
            if (provider == null) {
                provider = providerClass.getDeclaredConstructor().newInstance();
            }
            int installedPosition = Security.insertProviderAt(provider, position);
            log.info("Security Provider {} ({}) is installed at position {}", provider.getClass().getSimpleName(), provider.getName(), Integer.valueOf(installedPosition));
        } catch (Exception exception) {
            String message = String.format("Security Provider '%s' could not be installed.", classname);
            log.error(message, exception);
            System.err.print(message);
            System.err.println(" - see the log for more information.");
        }
    }
}