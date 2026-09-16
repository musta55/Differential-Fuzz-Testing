@Override
public OAuthServer update(OAuthServer server, Long userId) {
    persistOrMerge(server);
    cfgDao.updateCsp();
    return server;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isRegistrationAllowedViaOAuth() {
    return isAllowRegisterOauth();
}

private void persistOrMerge(OAuthServer server) {
    if (server.getId() == null) {
        em.persist(server);
    } else {
        em.merge(server);
    }
}

