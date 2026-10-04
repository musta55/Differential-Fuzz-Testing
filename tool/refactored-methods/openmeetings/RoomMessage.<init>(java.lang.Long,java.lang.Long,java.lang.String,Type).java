private RoomMessage(Long roomId, Long userId, String displayName, Type type) {
    this(new Date(), roomId, userId, displayName, type);
}
// ---- helper method(s) introduced by the refactoring ----
private RoomMessage(Date timestamp, Long roomId, Long userId, String displayName, Type type) {
    this.timestamp = timestamp;
    this.roomId = roomId;
    this.userId = userId;
    this.type = type;
    this.uid = randomUUID().toString();
    if (SIP_USER_ID.equals(userId)) {
        this.name = SIP_FIRST_NAME;
    } else {
        this.name = displayName;
    }
}

