public boolean unblock() {
    if (blocker == null) {
        return true;
    }
    if (client.send(blocker.buffer, blocker.dataOffset, blocker.length - (blocker.dataOffset - blocker.offset))) {
        blocker = null;
        return true;
    }
    return false;
}