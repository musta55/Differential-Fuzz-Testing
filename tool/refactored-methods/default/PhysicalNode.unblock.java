public boolean unblock() {
    if (isUnblocked()) {
        return true;
    }
    return attemptUnblock();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean canSendData(SerializedData d) {
    return client.send(d.buffer, d.dataOffset, d.length - (d.dataOffset - d.offset));
}

private void handleBlocking(SerializedData d) {
    if (blocker == null) {
        blocker = d;
    } else if (!blocker.equals(d)) {
        throw new IllegalStateException(String.format("Can't send data %s while blocker %s is pending on %s", d, blocker, this));
    }
}

private boolean isUnblocked() {
    return blocker == null;
}

private boolean attemptUnblock() {
    if (client.send(blocker.buffer, blocker.dataOffset, blocker.length - (blocker.dataOffset - blocker.offset))) {
        blocker = null;
        return true;
    }
    return false;
}

