public static void setupUserACLs(ContainerLaunchContext launchContext, String userName, Configuration conf) throws IOException {
    logger.debug("Setup login acls {}", userName);
    if (areACLsRequired(conf)) {
        logger.debug("Configuring ACLs for {}", userName);
        Map<ApplicationAccessType, String> acls = createACLs(userName);
        launchContext.setApplicationACLs(acls);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static Map<ApplicationAccessType, String> createACLs(String userName) {
    Map<ApplicationAccessType, String> acls = Maps.newHashMap();
    acls.put(ApplicationAccessType.VIEW_APP, userName);
    acls.put(ApplicationAccessType.MODIFY_APP, userName);
    return acls;
}

