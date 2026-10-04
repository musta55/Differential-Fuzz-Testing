public SOAPLogin get(String hash) {
    if (hash == null) {
        return null;
    }
    try {
        SOAPLogin sl = only(em.createNamedQuery("getSoapLoginByHash", SOAPLogin.class).setParameter("hash", hash).getResultList());
        if (sl != null && hash.equals(sl.getHash())) {
            return sl;
        } else {
            log.error("[get]: Wrong SOAPLogin was found by hash! {}", hash);
        }
    } catch (Exception ex) {
        log.error("[get]: ", ex);
    }
    return null;
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

