@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || !isWsClient(obj)) {
        return false;
    }
    WsClient other = (WsClient) obj;
    return uidEquals(other);
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

