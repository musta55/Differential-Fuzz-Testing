@Override
protected void onInitialize() {
    super.onInitialize();
    addComponents();
    addDeleteButton();
    addSaveButton();
}
// ---- helper method(s) introduced by the refactoring ----
private void addComponents() {
    add(toolbar);
    add(download);
}

private void addDeleteButton() {
    delBtn = new AjaxButton("delete", chatForm) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            deleteMessagesByScope(chatForm.getScope());
        }
    };
    delBtn.add(new ConfirmationBehavior(newOkCancelDangerConfirmCfg(this, getString("832")).withCustomClass("chat-delete")));
    toolbar.add(delBtn.setVisible(hasAdminLevel(getRights())).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
}

private void deleteMessagesByScope(String scope) {
    boolean admin = hasAdminLevel(getRights());
    chatForm.process(() -> {
        if (admin) {
            deleteGlobalMessages();
        }
        return true;
    }, r -> {
        if (admin || isModerator(cm, getUserId(), r.getId())) {
            deleteRoomMessages(r.getId(), scope);
        }
        return true;
    }, u -> {
        deleteUserMessages(u.getId(), scope);
        return true;
    });
}

private void deleteGlobalMessages() {
    chatDao.deleteGlobal();
    WebSocketHelper.sendAll(cleanMsg(ID_ALL).toString());
}

private void deleteRoomMessages(Long roomId, String scope) {
    chatDao.deleteRoom(roomId);
    WebSocketHelper.sendRoom(roomId, cleanMsg(scope));
}

private void deleteUserMessages(Long userId, String scope) {
    chatDao.deleteUser(userId);
    WebSocketHelper.sendUser(userId, cleanMsg(scope));
}

private void addSaveButton() {
    toolbar.add(save.setVisible(hasAdminLevel(getRights())).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).add(AjaxEventBehavior.onEvent(EVT_CLICK, download::initiate)));
}

private void updateVisibility(AjaxRequestTarget target, boolean saveVisible, boolean delBtnVisible) {
    target.add(save.setVisible(saveVisible), delBtn.setVisible(delBtnVisible));
}

