public String addSOAPLogin(String sessionHash, RoomOptionsDTO options) {
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
    em.persist(soapLogin);
    em.flush();
    Long soapLoginId = soapLogin.getId();
    if (soapLoginId != null) {
        return soapLogin.getHash();
    } else {
        log.error("[addSOAPLogin]: Could not store SOAPLogin");
    }
    return null;
}