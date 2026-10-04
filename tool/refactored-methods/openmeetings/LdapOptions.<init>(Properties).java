public LdapOptions(Properties config) {
    parseAndValidateConfiguration(config);
}
// ---- helper method(s) introduced by the refactoring ----
private void parseAndValidateConfiguration(Properties config) {
    useLowerCase = parseBoolean(config, CONFIGKEY_LDAP_USE_LOWER_CASE, false);
    type = parseEnum(config, CONFIGKEY_LDAP_AUTH_TYPE, AuthType.class, AuthType.SIMPLEBIND);
    prov = parseEnum(config, CONFIGKEY_LDAP_PROV_TYPE, Provisionning.class, Provisionning.AUTOCREATE);
    derefMode = parseAliasDerefMode(config, CONFIGKEY_LDAP_DEREF_MODE, AliasDerefMode.DEREF_ALWAYS);
    useAdminForAttrs = parseBoolean(config, CONFIGKEY_LDAP_USE_ADMIN_4ATTRS, true);
    groupMode = parseEnum(config, CONFIGKEY_LDAP_GROUP_MODE, GroupMode.class, GroupMode.NONE);
    validateAuthAndProvisioning(type, prov);
    validateAuthAndAdminUsage(type, useAdminForAttrs);
    host = config.getProperty(CONFIGKEY_LDAP_HOST);
    port = toInt(config.getProperty(CONFIGKEY_LDAP_PORT), 389);
    secure = parseBoolean(config, CONFIGKEY_LDAP_SECURE, false);
    adminDn = config.getProperty(CONFIGKEY_LDAP_ADMIN_DN);
    adminPasswd = config.getProperty(CONFIGKEY_LDAP_ADMIN_PASSWD);
    searchBase = config.getProperty(CONFIGKEY_LDAP_SEARCH_BASE, "");
    searchQuery = config.getProperty(CONFIGKEY_LDAP_SEARCH_QUERY, EMPTY_FORMAT);
    scope = parseEnum(config, CONFIGKEY_LDAP_SEARCH_SCOPE, SearchScope.class, SearchScope.ONELEVEL);
    syncPasswd = parseBoolean(config, CONFIGKEY_LDAP_SYNC_PASSWD_OM, false);
    tz = config.getProperty(CONFIGKEY_LDAP_TIMEZONE_NAME, null);
    groupQuery = config.getProperty(CONFIGKEY_LDAP_GROUP_QUERY, EMPTY_FORMAT);
    userDn = config.getProperty(CONFIGKEY_LDAP_USERDN_FORMAT, EMPTY_FORMAT);
    pictureUri = config.getProperty(CONFIGKEY_LDAP_PICTURE_URI, null);
    importQuery = config.getProperty(CONFIGKEY_LDAP_IMPORT_QUERY, "(objectClass=*)");
}

private boolean parseBoolean(Properties config, String key, boolean defaultValue) {
    return "true".equals(config.getProperty(key, String.valueOf(defaultValue)));
}

private <T extends Enum<T>> T parseEnum(Properties config, String key, Class<T> enumType, T defaultValue) {
    String value = config.getProperty(key, defaultValue.name());
    try {
        return Enum.valueOf(enumType, value);
    } catch (Exception e) {
        log.error("ConfigKey in Ldap Config contains invalid {} : '{}' -> Defaulting to {}", key, value, defaultValue);
        return defaultValue;
    }
}

private AliasDerefMode parseAliasDerefMode(Properties config, String key, AliasDerefMode defaultValue) {
    String value = config.getProperty(key, defaultValue.name());
    try {
        return AliasDerefMode.getDerefMode(value);
    } catch (Exception e) {
        log.error("ConfigKey in Ldap Config contains invalid {} : '{}' -> Defaulting to {}", key, value, defaultValue);
        return defaultValue;
    }
}

private void validateAuthAndProvisioning(AuthType type, Provisionning prov) {
    if (AuthType.NONE == type && Provisionning.NONE == prov) {
        throw new RuntimeException("Both AuthType and Provisionning are NONE!");
    }
}

private void validateAuthAndAdminUsage(AuthType type, boolean useAdminForAttrs) {
    if (AuthType.NONE == type && !useAdminForAttrs) {
        throw new RuntimeException("Unable to get Attributes, please change Auth type and/or Use Admin to get attributes");
    }
}

