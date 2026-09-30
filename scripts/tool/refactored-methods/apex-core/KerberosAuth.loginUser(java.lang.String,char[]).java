public static Subject loginUser(String principal, char[] password) throws LoginException, IOException {
    Subject subject = new Subject();
    LoginContext lc = new LoginContext(KerberosAuth.class.getName(), subject, new AuthenticationHandler(principal, password), new KerberosConfiguration(principal));
    lc.login();
    return subject;
    //return UserGroupInformation.getUGIFromTicketCache(ticketCache, principal);
}