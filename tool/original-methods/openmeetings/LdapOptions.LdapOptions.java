public LdapOptions(Properties config) {
    String useLowerCaseProp = config.getProperty(CONFIGKEY_LDAP_USE_LOWER_CASE, "false");
    useLowerCase = "true".equals(useLowerCaseProp);
    String authType = config.getProperty(CONFIGKEY_LDAP_AUTH_TYPE, "");
    try {
        type = AuthType.valueOf(authType);
    } catch (Exception e) {
        log.error("ConfigKey in Ldap Config contains invalid auth type : '{}' -> Defaulting to {}", authType, type);
    }
    String provType = config.getProperty(CONFIGKEY_LDAP_PROV_TYPE, "");
    try {
        prov = Provisionning.valueOf(provType);
    } catch (Exception e) {
        log.error("ConfigKey in Ldap Config contains invalid provisionning type : '{}' -> Defaulting to {}", provType, prov);
    }
    String derefModeProp = config.getProperty(CONFIGKEY_LDAP_DEREF_MODE, "");
    try {
        derefMode = AliasDerefMode.getDerefMode(derefModeProp);
    } catch (Exception e) {
        log.error("ConfigKey in Ldap Config contains invalid deref mode : '{}' -> Defaulting to {}", derefModeProp, derefMode);
    }
    if (AuthType.NONE == type && Provisionning.NONE == prov) {
        throw new RuntimeException("Both AuthType and Provisionning are NONE!");
    }
    try {
        useAdminForAttrs = "true".equals(config.getProperty(CONFIGKEY_LDAP_USE_ADMIN_4ATTRS, ""));
    } catch (Exception e) {
        //no-op
    }
    try {
        groupMode = GroupMode.valueOf(config.getProperty(CONFIGKEY_LDAP_GROUP_MODE, "NONE"));
    } catch (Exception e) {
        //no-op
    }
    if (AuthType.NONE == type && !useAdminForAttrs) {
        throw new RuntimeException("Unable to get Attributes, please change Auth type and/or Use Admin to get attributes");
    }
    // Connection URL
    host = config.getProperty(CONFIGKEY_LDAP_HOST);
    port = toInt(config.getProperty(CONFIGKEY_LDAP_PORT), 389);
    secure = "true".equals(config.getProperty(CONFIGKEY_LDAP_SECURE, "false"));
    // Username for LDAP SERVER himself
    adminDn = config.getProperty(CONFIGKEY_LDAP_ADMIN_DN);
    // Password for LDAP SERVER himself
    adminPasswd = config.getProperty(CONFIGKEY_LDAP_ADMIN_PASSWD);
    searchBase = config.getProperty(CONFIGKEY_LDAP_SEARCH_BASE, "");
    searchQuery = config.getProperty(CONFIGKEY_LDAP_SEARCH_QUERY, EMPTY_FORMAT);
    scope = SearchScope.valueOf(config.getProperty(CONFIGKEY_LDAP_SEARCH_SCOPE, SearchScope.ONELEVEL.name()));
    syncPasswd = "true".equals(config.getProperty(CONFIGKEY_LDAP_SYNC_PASSWD_OM, ""));
    tz = config.getProperty(CONFIGKEY_LDAP_TIMEZONE_NAME, null);
    groupQuery = config.getProperty(CONFIGKEY_LDAP_GROUP_QUERY, EMPTY_FORMAT);
    userDn = config.getProperty(CONFIGKEY_LDAP_USERDN_FORMAT, EMPTY_FORMAT);
    pictureUri = config.getProperty(CONFIGKEY_LDAP_PICTURE_URI, null);
    importQuery = config.getProperty(CONFIGKEY_LDAP_IMPORT_QUERY, "(objectClass=*)");
}