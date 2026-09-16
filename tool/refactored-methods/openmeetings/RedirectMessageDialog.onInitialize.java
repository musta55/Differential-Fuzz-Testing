@Override
protected void onInitialize() {
    header(new ResourceModel("204"));
    setCloseOnEscapeKey(false);
    show(autoOpen);
    withLabel(new ResourceModel(labelId));
    getLabel().setOutputMarkupId(true);
    withErrorIcon();
    super.onInitialize();
    if (autoOpen) {
        initiateTimer();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void initiateTimer() {
    startTimer(null);
}

private OmTimerBehavior createOmTimerBehavior() {
    return new OmTimerBehavior(DELAY, labelId) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onFinish(AjaxRequestTarget target) {
            handleFinish();
        }
    };
}

private void handleFinish() {
    if (Strings.isEmpty(url)) {
        throw new RestartResponseException(Application.get().getHomePage());
    } else {
        throw new RedirectToUrlException(url);
    }
}

