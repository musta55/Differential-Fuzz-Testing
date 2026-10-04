@Override
public String toString() {
    return shouldSuppressStacktrace() ? getLocalizedMessage() : super.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldSuppressStacktrace() {
    return suppressStacktrace;
}

