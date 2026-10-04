@Override
public void renderHead(IHeaderResponse response) {
    super.renderHead(response);
    renderChatJs(response);
    renderChatActivity(response);
    if (showDashboardChat) {
        renderDashboardChat(response);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void renderChatJs(IHeaderResponse response) {
    response.render(new PriorityHeaderItem(new JavaScriptUrlReferenceHeaderItem("js/chat.js", "om-chat") {

        private static final long serialVersionUID = 1L;

        @Override
        public List<HeaderItem> getDependencies() {
            return List.of(new PriorityHeaderItem(JavaScriptHeaderItem.forScript("const bstooltip = jQuery.fn.tooltip;", "preserve-bs-tooltip")), new PriorityHeaderItem(JavaScriptHeaderItem.forReference(JQueryUILibrarySettings.get().getJavaScriptReference())), new PriorityHeaderItem(JavaScriptHeaderItem.forScript("jQuery.fn.tooltip = bstooltip;", "restore-bs-tooltip")));
        }
    }));
}

private void renderChatActivity(IHeaderResponse response) {
    response.render(new PriorityHeaderItem(getNamedFunction("chatActivity", chatActivity, explicit(PARAM_TYPE), explicit(PARAM_ROOM_ID), explicit(PARAM_MSG_ID))));
}

private void renderDashboardChat(IHeaderResponse response) {
    StringBuilder sb = new StringBuilder(getReinit());
    List<ChatMessage> list = new ArrayList<>(chatDao.getGlobal(0, 30));
    list.addAll(chatDao.getUserRecent(getUserId(), Date.from(Instant.now().minus(Duration.ofHours(1L))), 0, 30));
    if (!list.isEmpty()) {
        sb.append("Chat.addMessage(").append(getMessage(list).toString()).append(");");
    }
    response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
}

