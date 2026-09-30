public static boolean areACLsRequired(Configuration conf) {
    logger.debug("Check ACLs required");
    if (conf.getBoolean(YarnConfiguration.YARN_ACL_ENABLE, YarnConfiguration.DEFAULT_YARN_ACL_ENABLE)) {
        logger.debug("Admin ACL {}", conf.get(YarnConfiguration.YARN_ADMIN_ACL));
        if (!YarnConfiguration.DEFAULT_YARN_ADMIN_ACL.equals(conf.get(YarnConfiguration.YARN_ADMIN_ACL))) {
            logger.debug("Non default admin ACL");
            return true;
        }
    }
    return false;
}