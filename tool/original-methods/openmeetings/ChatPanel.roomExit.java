public void roomExit(Room r, IPartialPageRequestHandler handler) {
    if (r.isHidden(RoomElement.CHAT)) {
        return;
    }
    handler.appendJavaScript(String.format("if (typeof(Chat) === 'object') { Chat.removeTab('%1$s%2$d'); }", ID_ROOM_PREFIX, r.getId()));
    StringBuilder sb = new StringBuilder("$(function() {").append("Chat.setRoomMode(false);");
    if (!chat.isShowDashboardChat()) {
        sb.append("$('#chatPanel').hide();");
    }
    sb.append("});");
    handler.appendJavaScript(sb);
}