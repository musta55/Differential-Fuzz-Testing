private void muteRoomAction(String uid, Client self, JSONObject o) {
    Client c = cm.get(uid);
    if (c == null || !c.has(Client.Activity.AUDIO)) {
        return;
    }
    if (self.hasRight(Right.MODERATOR) || self.getUid().equals(c.getUid())) {
        WebSocketHelper.sendRoom(new TextRoomMessage(room.getRoom().getId(), self, RoomMessage.Type.MUTE, new JSONObject().put("sid", self.getSid()).put(PARAM_UID, uid).put("mute", o.getBoolean("mute")).toString()));
    }
}