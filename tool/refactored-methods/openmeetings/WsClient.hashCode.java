@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + hashUid();
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isWsClient(Object obj) {
    return obj instanceof WsClient;
}

private boolean uidEquals(WsClient other) {
    if (uid == null) {
        return other.uid == null;
    }
    return uid.equals(other.uid);
}

private int hashUid() {
    return uid == null ? 0 : uid.hashCode();
}

