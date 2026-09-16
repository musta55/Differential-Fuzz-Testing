@Override
protected void onInitialize() {
    super.onInitialize();
    add(toolbar);
    add(download);
    delBtn = new AjaxButton("delete", chatForm) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final String scope = chatForm.getScope();
            final boolean admin = hasAdminLevel(getRights());
            chatForm.process(() -> {
                if (admin) {
                    chatDao.deleteGlobal();
                    WebSocketHelper.sendAll(cleanMsg(ID_ALL).toString());
                }
                return true;
            }, r -> {
                if (admin || isModerator(cm, getUserId(), r.getId())) {
                    chatDao.deleteRoom(r.getId());
                    WebSocketHelper.sendRoom(r.getId(), cleanMsg(scope));
                }
                return true;
            }, u -> {
                chatDao.deleteUser(u.getId());
                WebSocketHelper.sendUser(u.getId(), cleanMsg(scope));
                return true;
            });
        }
    };
    delBtn.add(new ConfirmationBehavior(newOkCancelDangerConfirmCfg(this, getString("832")).withCustomClass("chat-delete")));
    toolbar.add(delBtn.setVisible(hasAdminLevel(getRights())).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
    toolbar.add(save.setVisible(hasAdminLevel(getRights())).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).add(AjaxEventBehavior.onEvent(EVT_CLICK, download::initiate)));
}