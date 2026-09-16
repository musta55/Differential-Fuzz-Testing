@Override
public String marshal(Recording.Status v) throws Exception {
    return v == null ? Recording.Status.NONE.name() : v.name();
}
// ---- helper method(s) introduced by the refactoring ----
private Recording.Status safeValueOf(String v) {
    try {
        return Recording.Status.valueOf(v);
    } catch (Exception e) {
        //no-op
        return Status.NONE;
    }
}

