<T> T performCall(String sid, Predicate<Sessiondata> allowed, Function<Sessiondata, T> action) throws ServiceException {
    return performCallWithPredicate(sid, allowed, action);
}
// ---- helper method(s) introduced by the refactoring ----
private <T> T performCallWithPredicate(String sid, Predicate<Sessiondata> allowed, Function<Sessiondata, T> action) throws ServiceException {
    try {
        Sessiondata sd = check(sid);
        if (allowed.test(sd)) {
            return action.apply(sd);
        } else {
            throw NO_PERMISSION;
        }
    } catch (ServiceException err) {
        throw err;
    } catch (Exception err) {
        log.error("[performCall]", err);
        throw new ServiceException(err.getMessage());
    }
}

