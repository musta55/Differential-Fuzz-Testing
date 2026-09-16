public ConferenceLog add(Type type, Long userId, String streamid, Long roomId, String userip, String scopeName) {
    ConferenceLog confLog = new ConferenceLog();
    confLog.setType(type);
    confLog.setInserted(new Date());
    confLog.setUserId(userId);
    confLog.setStreamid(streamid);
    confLog.setScopeName(scopeName);
    confLog.setRoomId(roomId);
    confLog.setUserip(userip);
    em.persist(confLog);
    log.debug("[add]: {}", confLog);
    return confLog;
}