@Override
protected void onBind() {
    super.onBind();
    setComponentModel(getText(delay));
    getComponent().setOutputMarkupId(true);
    handleTimerLogic(delay);
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateRemainingTime(long now) {
    return (int) (delay - (now - clock) / 1000);
}

private void setComponentModel(String text) {
    getComponent().setDefaultModelObject(text);
}

private void handleTimerLogic(int remain) {
    onTimer(remain);
}

