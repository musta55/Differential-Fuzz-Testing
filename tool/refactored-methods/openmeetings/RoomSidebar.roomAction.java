public void roomAction(IPartialPageRequestHandler handler, JSONObject o) {
    try {
        String uid = o.getString(PARAM_UID);
        if (Strings.isEmpty(uid)) {
            return;
        }
        Client self = room.getClient();
        Action a = Action.of(o.getString(PARAM_ACTION));
        switch(a) {
            case KICK:
                handleKickAction(self, uid);
                break;
            case MUTE_OTHERS:
                handleMuteOthersAction(self, uid);
                break;
            case MUTE:
                muteRoomAction(uid, self, o);
                break;
            case TOGGLE_RIGHT:
                toggleRight(handler, self, uid, o);
                break;
            default:
        }
    } catch (Exception e) {
        log.error("Unexpected exception while processing 'roomAction'", e);
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

