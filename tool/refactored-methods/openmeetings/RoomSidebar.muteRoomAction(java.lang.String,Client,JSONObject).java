private void muteRoomAction(String uid, Client self, JSONObject o) {
    Client c = cm.get(uid);
    if (c != null && c.has(Client.Activity.AUDIO)) {
        if (self.hasRight(Right.MODERATOR) || self.getUid().equals(c.getUid())) {
            WebSocketHelper.sendRoom(new TextRoomMessage(room.getRoom().getId(), self, RoomMessage.Type.MUTE, new JSONObject().put("sid", self.getSid()).put(PARAM_UID, uid).put("mute", o.getBoolean("mute")).toString()));
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private NameDialog createAddFolderDialog() {
    return new NameDialog("addFolder", getString("712")) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            roomFiles.createFolder(target, getModelObject());
            super.onSubmit(target);
        }
    };
}

private void handleKickAction(Client self, String uid) {
    if (self.hasRight(Right.MODERATOR)) {
        Client kickedClient = cm.get(uid);
        if (kickedClient != null && !kickedClient.hasRight(Right.SUPER_MODERATOR) && !self.getUid().equals(kickedClient.getUid())) {
            kickUser(kickedClient);
        }
    }
}

private void handleMuteOthersAction(Client self, String uid) {
    if (self.hasRight(Right.MUTE_OTHERS)) {
        WebSocketHelper.sendRoom(new TextRoomMessage(room.getRoom().getId(), self, RoomMessage.Type.MUTE_OTHERS, uid));
    }
}

