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
    try {
        return em.createNamedQuery("getSessionById", Sessiondata.class).setParameter("sessionId", sid).getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
}