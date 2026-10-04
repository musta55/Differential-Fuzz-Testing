public ConferenceLog add(Type type, Long userId, String streamid, Long roomId, String userip, String scopeName) {
    ConferenceLog confLog = buildConferenceLog(type, userId, streamid, roomId, userip, scopeName);
    em.persist(confLog);
    log.debug("[add]: {}", confLog);
    return confLog;
}
// ---- helper method(s) introduced by the refactoring ----
private ConferenceLog buildConferenceLog(Type type, Long userId, String streamid, Long roomId, String userip, String scopeName) {
    ConferenceLog confLog = new ConferenceLog();
    confLog.setType(type);
    confLog.setInserted(new Date());
    confLog.setUserId(userId);
    confLog.setStreamid(streamid);
    confLog.setScopeName(scopeName);
    confLog.setRoomId(roomId);
    confLog.setUserip(userip);
    return confLog;
}

