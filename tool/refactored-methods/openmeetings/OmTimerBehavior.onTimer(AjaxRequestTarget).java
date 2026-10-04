@Override
protected void onTimer(AjaxRequestTarget target) {
    int remain = calculateRemainingTime(System.currentTimeMillis());
    if (remain > -1) {
        setComponentModel(getText(remain));
        handleTimerLogic(remain);
        target.add(getComponent());
    } else {
        stop(target);
        onFinish(target);
    }
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

