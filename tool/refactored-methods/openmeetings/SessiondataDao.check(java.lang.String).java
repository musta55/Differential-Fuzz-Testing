/**
 * @param sid - sid of {@link Sessiondata} to check
 * @return - {@link Sessiondata} for given sid or new {@link Sessiondata}
 */
public Sessiondata check(String sid) {
    Sessiondata sd = find(sid);
    // Checks if wether the Session or the User Object of that Session is set yet
    if (sd == null || sd.getUserId() == null || sd.getUserId().equals(Long.valueOf(0))) {
        return newInstance();
    }
    return update(sd);
}