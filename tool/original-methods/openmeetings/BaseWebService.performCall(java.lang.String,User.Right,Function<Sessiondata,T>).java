<T> T performCall(String sid, User.Right level, Function<Sessiondata, T> action) throws ServiceException {
    return performCall(sid, sd -> AuthLevelUtil.check(getRights(sd.getUserId()), level), action);
}