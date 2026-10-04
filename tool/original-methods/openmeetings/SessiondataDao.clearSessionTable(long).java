/**
 * @param timeout - timeout in millis to check expired sessions
 */
public void clearSessionTable(long timeout) {
    try {
        log.trace("****** clearSessionTable: ");
        List<Sessiondata> l = getSessionToDelete(new Date(System.currentTimeMillis() - timeout));
        if (!l.isEmpty()) {
            log.debug("clearSessionTable: {}", l.size());
            for (Sessiondata sData : l) {
                sData = em.find(Sessiondata.class, sData.getId());
                em.remove(sData);
            }
        }
    } catch (Exception err) {
        log.error("clearSessionTable", err);
    }
}