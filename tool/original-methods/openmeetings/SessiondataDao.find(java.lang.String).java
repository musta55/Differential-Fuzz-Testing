/**
 * Serches {@link Sessiondata} object by sessionId
 *
 * @param sid - sessionId
 * @return {@link Sessiondata} with sessionId == SID, or null if not found
 */
public Sessiondata find(String sid) {
    if (sid == null) {
        return null;
    }
    //MSSql find nothing in case SID is passed as-is without wildcarting '%SID%'
    List<Sessiondata> sessions = em.createNamedQuery("getSessionById", Sessiondata.class).setParameter("sessionId", String.format("%%%s%%", sid)).getResultList();
    if (sessions.isEmpty()) {
        return null;
    }
    Sessiondata sd = sessions.get(0);
    if (sd == null || sd.getUserId() == null || sd.getUserId().equals(Long.valueOf(0)) || !sid.equals(sd.getSessionId())) {
        return null;
    }
    return sd;
}