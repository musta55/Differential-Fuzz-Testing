@Override
public Room.RoomElement unmarshal(String v) throws Exception {
    if ("TopBar".equals(v)) {
        return Room.RoomElement.TOP_BAR;
    }
    if ("ActionMenu".equals(v)) {
        return Room.RoomElement.ACTION_MENU;
    }
    if ("PollMenu".equals(v)) {
        return Room.RoomElement.POLL_MENU;
    }
    if ("ScreenSharing".equals(v)) {
        return Room.RoomElement.SCREEN_SHARING;
    }
    if ("MicrophoneStatus".equals(v)) {
        return Room.RoomElement.MICROPHONE_STATUS;
    }
    if ("UserCount".equals(v)) {
        return Room.RoomElement.USER_COUNT;
    }
    return Strings.isEmpty(v) ? null : Room.RoomElement.valueOf(v.toUpperCase(Locale.ROOT));
}