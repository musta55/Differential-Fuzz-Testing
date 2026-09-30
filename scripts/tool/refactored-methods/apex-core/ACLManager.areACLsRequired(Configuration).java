public static boolean areACLsRequired(Configuration conf) {
    logger.debug("Check ACLs required");
    boolean aclEnabled = conf.getBoolean(YarnConfiguration.YARN_ACL_ENABLE, YarnConfiguration.DEFAULT_YARN_ACL_ENABLE);
    if (aclEnabled) {
        logger.debug("Admin ACL {}", conf.get(YarnConfiguration.YARN_ADMIN_ACL));
        String adminAcl = conf.get(YarnConfiguration.YARN_ADMIN_ACL);
        if (!YarnConfiguration.DEFAULT_YARN_ADMIN_ACL.equals(adminAcl)) {
            logger.debug("Non default admin ACL");
            return true;
        }
    }
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private static Map<ApplicationAccessType, String> createACLs(String userName) {
    Map<ApplicationAccessType, String> acls = Maps.newHashMap();
    acls.put(ApplicationAccessType.VIEW_APP, userName);
    acls.put(ApplicationAccessType.MODIFY_APP, userName);
    return acls;
}

