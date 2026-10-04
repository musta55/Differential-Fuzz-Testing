protected void toBuffer(H5BufferBuilder bb, int messageType) {
    // Message Type
    bb.writeShort((short) messageType);
    byte[] reserved = { (byte) 0, 0, 0 };
    int dataSize = getMessageSize(messageType);
    bb.writeShort((short) dataSize);
    // Flags
    if (flags.length() != 0) {
        bb.writeBitSet(flags, flags.length());
    } else {
        bb.writeByte(0);
    }
    // Skip 3 reserved zero bytes
    bb.writeBytes(reserved);
}
// ---- helper method(s) introduced by the refactoring ----
private int getMessageSize(int messageType) {
    switch(messageType) {
        case H5Constants.NIL_MESSAGE:
            return 104;
        case H5Constants.DATA_SPACE_MESSAGE:
            return 40;
        case H5Constants.DATA_TYPE_MESSAGE:
            return 24;
        case H5Constants.FILL_VALUE_MESSAGE:
            return 8;
        case H5Constants.SYMBOL_TABLE_MESSAGE:
            return 16;
        case H5Constants.OBJECT_MODIFICATION_TIME_MESSAGE:
            return 8;
        case H5Constants.DATA_LAYOUT_MESSAGE:
            return 24;
        default:
            throw new H5RuntimeException("Unrecognized message type = " + messageType);
    }
}

