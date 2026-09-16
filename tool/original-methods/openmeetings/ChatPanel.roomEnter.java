public void roomEnter(Room r, IPartialPageRequestHandler handler) {
    if (r.isHidden(RoomElement.CHAT)) {
        toggle(handler, false);
        return;
    }
    StringBuilder sb = new StringBuilder("$(function() {");
    if (!chat.isShowDashboardChat()) {
        sb.append("$('#chatPanel').show();");
    }
    sb.append("Chat.setRoomMode(true);").append(chat.addRoom(r)).append("Chat.").append(r.isChatOpened() ? "setOpened" : "close").append("();");
    chat.processGlobal(sb);
    sb.append("});");
    handler.appendJavaScript(sb);
}