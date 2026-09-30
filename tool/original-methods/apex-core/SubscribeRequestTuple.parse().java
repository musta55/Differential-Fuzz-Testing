@Override
public void parse() {
    parsed = true;
    try {
        /*
       * read the version.
       */
        int idlen = readVarInt();
        if (idlen > 0) {
            version = new String(buffer, offset, idlen);
            offset += idlen;
        } else if (idlen == 0) {
            version = EMPTY_STRING;
        } else {
            return;
        }
        /*
       * read the identifier.
       */
        idlen = readVarInt();
        if (idlen > 0) {
            identifier = new String(buffer, offset, idlen);
            offset += idlen;
        } else if (idlen == 0) {
            identifier = EMPTY_STRING;
        } else {
            return;
        }
        baseSeconds = readVarInt();
        windowId = readVarInt();
        /*
       * read the type
       */
        idlen = readVarInt();
        if (idlen > 0) {
            streamType = new String(buffer, offset, idlen);
            offset += idlen;
        } else if (idlen == 0) {
            streamType = EMPTY_STRING;
        } else {
            return;
        }
        /*
       * read the upstream identifier
       */
        idlen = readVarInt();
        if (idlen > 0) {
            upstreamIdentifier = new String(buffer, offset, idlen);
            offset += idlen;
        } else if (idlen == 0) {
            upstreamIdentifier = EMPTY_STRING;
        } else {
            return;
        }
        /*
       * read the partition count
       */
        int count = readVarInt();
        if (count > 0) {
            mask = readVarInt();
            if (mask <= 0) {
                /* mask cannot be zero */
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
        bufferSize = readVarInt();
        if (bufferSize == -1) {
            return;
        }
        valid = true;
    } catch (NumberFormatException nfe) {
        logger.warn("Unparseable Tuple", nfe);
    }
}