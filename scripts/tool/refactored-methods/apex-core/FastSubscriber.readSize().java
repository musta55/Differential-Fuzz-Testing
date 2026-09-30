@Override
public int readSize() {
    if (writeOffset - readOffset < 2) {
        return -1;
    }
    return calculateSize();
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateSize() {
    short s = buffer[readOffset++];
    return s | (buffer[readOffset++] << 8);
}

