@Override
public void parse() {
    parsed = true;
    try {
        version = readString();
        identifier = readString();
        baseSeconds = readVarInt();
        windowId = readVarInt();
        streamType = readString();
        upstreamIdentifier = readString();
        readPartitions();
        bufferSize = readVarInt();
        if (bufferSize == -1) {
            return;
        }
        valid = true;
    } catch (NumberFormatException nfe) {
        logger.warn("Unparseable Tuple", nfe);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private String readString() {
    int idlen = readVarInt();
    if (idlen > 0) {
        String result = new String(buffer, offset, idlen);
        offset += idlen;
        return result;
    } else if (idlen == 0) {
        return EMPTY_STRING;
    } else {
        return null;
    }
}

private void readPartitions() {
    int count = readVarInt();
    if (count > 0) {
        mask = readVarInt();
        if (mask <= 0) {
            return;
        }
        partitions = new int[count];
        for (int i = 0; i < count; i++) {
            partitions[i] = readVarInt();
            if (partitions[i] == -1) {
                return;
            }
        }
    }
}

