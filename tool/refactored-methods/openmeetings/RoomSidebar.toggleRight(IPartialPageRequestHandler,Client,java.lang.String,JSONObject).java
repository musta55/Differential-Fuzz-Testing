private void toggleRight(IPartialPageRequestHandler handler, Client self, String uid, JSONObject o) {
    try {
        Right right = Right.valueOf(o.getString(PARAM_RIGHT));
        if (self.hasRight(Right.MODERATOR)) {
            Client client = cm.get(uid);
            if (client != null) {
                if (client.hasRight(right)) {
                    room.denyRight(client, right);
                } else {
                    if (Right.VIDEO == right) {
                        room.allowRight(client, Right.AUDIO, right);
                    } else {
                        room.allowRight(client, right);
                    }
                }
            }
        } else {
            room.requestRight(right, handler);
        }
    } catch (Exception e) {
        log.error("Unexpected exception while toggling 'right'", e);
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

