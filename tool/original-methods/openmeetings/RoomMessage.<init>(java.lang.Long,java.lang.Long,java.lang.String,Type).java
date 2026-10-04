private RoomMessage(Long roomId, Long userId, String displayName, Type type) {
    this.timestamp = new Date();
    this.roomId = roomId;
    if (SIP_USER_ID.equals(userId)) {
        this.name = SIP_FIRST_NAME;
    } else {
        name = displayName;
    }
    this.userId = userId;
    this.type = type;
    this.uid = randomUUID().toString();
}