public List<OAuthServer> getActive() {
    if (!isAllowRegisterOauth()) {
        return List.of();
    }
    return em.createNamedQuery("getEnabledOAuthServers", OAuthServer.class).getResultList();
}