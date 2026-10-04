@Override
public void configure(TestElement el) {
    super.configure(el);
    if (el instanceof DurationAssertion) {
        DurationAssertion da = (DurationAssertion) el;
        duration.setText(da.getPropertyAsString(DurationAssertion.DURATION_KEY));
        showScopeSettings(da);
    }
}