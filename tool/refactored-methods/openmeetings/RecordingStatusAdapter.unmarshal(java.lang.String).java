@Override
public Recording.Status unmarshal(String v) throws Exception {
    if ("PROCESSING".equalsIgnoreCase(v)) {
        return Recording.Status.CONVERTING;
    }
    return !Strings.isEmpty(v) ? safeValueOf(v) : Status.NONE;
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

