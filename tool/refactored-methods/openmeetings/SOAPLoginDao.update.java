public void update(SOAPLogin soapLogin) {
    try {
        if (soapLogin.getId() == null) {
            em.persist(soapLogin);
        } else if (!em.contains(soapLogin)) {
            em.merge(soapLogin);
        }
    } catch (Exception ex) {
        log.error("[update]: ", ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private SOAPLogin createSOAPLogin(String sessionHash, RoomOptionsDTO options) {
    SOAPLogin soapLogin = new SOAPLogin();
    soapLogin.setCreated(new Date());
    soapLogin.setUsed(false);
    soapLogin.setRoomId(options.getRoomId());
    soapLogin.setExternalRoomId(options.getExternalRoomId());
    soapLogin.setExternalType(options.getExternalType());
    soapLogin.setAllowSameURLMultipleTimes(options.isAllowSameURLMultipleTimes());
    soapLogin.setHash(randomUUID().toString());
    soapLogin.setRecordingId(options.getRecordingId());
    soapLogin.setSessionHash(sessionHash);
    soapLogin.setModerator(options.isModerator());
    soapLogin.setShowAudioVideoTest(options.isShowAudioVideoTest());
    soapLogin.setAllowRecording(options.isAllowRecording());
    return soapLogin;
}

private void persistSOAPLogin(SOAPLogin soapLogin) {
    em.persist(soapLogin);
    em.flush();
    if (soapLogin.getId() == null) {
        log.error("[addSOAPLogin]: Could not store SOAPLogin");
    }
}

