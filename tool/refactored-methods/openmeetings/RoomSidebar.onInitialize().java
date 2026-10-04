@Override
protected void onInitialize() {
    super.onInitialize();
    NameDialog addFolder = createAddFolderDialog();
    roomFiles = new RoomFilePanel("tree", room, addFolder);
    fileTab.setOutputMarkupId(true);
    roomFiles.setOutputMarkupId(true);
    add(fileTab.setVisible(!room.isInterview()), roomFiles.setVisible(!room.isInterview()));
    add(addFolder, settings);
    add(upload = new UploadDialog("upload", roomFiles));
    updateShowFiles(null);
    add(activities = new ActivitiesPanel("activities", room));
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

