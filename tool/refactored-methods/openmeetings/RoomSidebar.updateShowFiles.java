private void updateShowFiles(IPartialPageRequestHandler handler) {
    if (room.isInterview()) {
        return;
    }
    boolean newShowFiles = !room.getRoom().isHidden(RoomElement.FILES) && room.getClient().hasRight(Right.PRESENTER);
    if (showFiles != newShowFiles) {
        showFiles = newShowFiles;
        roomFiles.setReadOnly(!showFiles, handler);
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

