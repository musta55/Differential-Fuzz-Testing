@Override
public OAuthServer update(OAuthServer server, Long userId) {
    if (server.getId() == null) {
        em.persist(server);
    } else {
        server = em.merge(server);
    }
    cfgDao.updateCsp();
    return server;
}