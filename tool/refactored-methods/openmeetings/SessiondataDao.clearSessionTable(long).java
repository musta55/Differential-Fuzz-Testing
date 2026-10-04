/**
 * @param timeout - timeout in millis to check expired sessions
 */
public void clearSessionTable(long timeout) {
    log.trace("****** clearSessionTable: ");
    List<Sessiondata> l = getSessionToDelete(new Date(System.currentTimeMillis() - timeout));
    if (!l.isEmpty()) {
        log.debug("clearSessionTable: {}", l.size());
        for (Sessiondata sData : l) {
            em.remove(sData);
        }
    }
}