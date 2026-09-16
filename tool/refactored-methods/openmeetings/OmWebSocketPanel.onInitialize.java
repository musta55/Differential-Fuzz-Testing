@Override
protected void onInitialize() {
    super.onInitialize();
    add(newWsBehavior());
    addPingBehavior();
}
// ---- helper method(s) introduced by the refactoring ----
private void addPingBehavior() {
    add(new Behavior() {

        private static final long serialVersionUID = 1L;

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            if (!pingable) {
                log.debug("pingTimer is attached");
                pingable = true;
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript("OmUtil.ping();"));
            }
        }
    });
}

