public List<OAuthServer> getActive() {
    if (!isRegistrationAllowedViaOAuth()) {
        return List.of();
    }
    return em.createNamedQuery("getEnabledOAuthServers", OAuthServer.class).getResultList();
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

