public static void read(SerializedData current) {
    final byte[] data = current.buffer;
    int offset = current.offset;
    int result = 0;
    int shift = 0;
    while (true) {
        byte tmp = data[offset++];
        if ((tmp & 0x80) == 0) {
            result |= (tmp & 0x7f) << shift;
            break;
        }
        result |= (tmp & 0x7f) << shift;
        shift += 7;
        if (shift >= 32) {
            handleOverflow(data, offset, current);
            return;
        }
    }
    current.dataOffset = offset;
    current.length = result + offset - current.offset;
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleOverflow(byte[] data, int offset, SerializedData current) {
    for (int i = 0; i < 5; i++) {
        if (data[offset++] >= 0) {
            current.dataOffset = offset;
            current.length = -1;
            return;
        }
    }
    current.length = -1;
}

