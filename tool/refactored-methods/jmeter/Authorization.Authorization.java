/**
 * create the authorization
 * @param url url for which the authorization should be considered
 * @param user name of the user
 * @param pass password for the user
 * @param domain authorization domain (used for NTML-authentication)
 * @param realm authorization realm
 * @param mechanism authorization mechanism, that should be used
 */
Authorization(String url, String user, String pass, String domain, String realm, Mechanism mechanism) {
    initialize(url, user, pass, domain, realm, mechanism);
}
// ---- helper method(s) introduced by the refactoring ----
private void initialize(String url, String user, String pass, String domain, String realm, Mechanism mechanism) {
    setURL(url);
    setUser(user);
    setPass(pass);
    setDomain(domain);
    setRealm(realm);
    setMechanism(mechanism);
}

