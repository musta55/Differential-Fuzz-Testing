public void update(SOAPLogin soapLogin) {
    try {
        if (soapLogin.getId() == null) {
            em.persist(soapLogin);
        } else {
            if (!em.contains(soapLogin)) {
                em.merge(soapLogin);
            }
        }
    } catch (Exception ex2) {
        log.error("[update]: ", ex2);
    }
}