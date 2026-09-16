public void roomExit(Room r, IPartialPageRequestHandler handler) {
    if (r.isHidden(RoomElement.CHAT)) {
        return;
    }
    handler.appendJavaScript(buildRoomExitJavaScript(r));
    StringBuilder sb = new StringBuilder("$(function() {").append("Chat.setRoomMode(false);");
    if (!chat.isShowDashboardChat()) {
        sb.append("$('#chatPanel').hide();");
    }
    sb.append("});");
    handler.appendJavaScript(sb);
}
// ---- helper method(s) introduced by the refactoring ----
private String buildRoomEnterJavaScript(Room r) {
    StringBuilder sb = new StringBuilder("$(function() {");
    if (!chat.isShowDashboardChat()) {
        sb.append("$('#chatPanel').show();");
    }
    sb.append("Chat.setRoomMode(true);").append(chat.addRoom(r)).append("Chat.").append(r.isChatOpened() ? "setOpened" : "close").append("();");
    return sb.toString();
}

private String buildRoomExitJavaScript(Room r) {
    return String.format("if (typeof(Chat) === 'object') { Chat.removeTab('%1$s%2$d'); }", ID_ROOM_PREFIX, r.getId());
}

