@Override
public void configure(TestElement el) {
    super.configure(el);
    if (el instanceof DurationAssertion) {
        DurationAssertion da = (DurationAssertion) el;
        setDurationText(da);
        showScopeSettings(da);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setDurationProperty(DurationAssertion assertion) {
    assertion.setProperty(DurationAssertion.DURATION_KEY, duration.getText());
}

private void setDurationText(DurationAssertion da) {
    duration.setText(da.getPropertyAsString(DurationAssertion.DURATION_KEY));
}

