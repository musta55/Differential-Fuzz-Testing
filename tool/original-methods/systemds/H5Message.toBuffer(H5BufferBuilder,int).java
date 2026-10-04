protected void toBuffer(H5BufferBuilder bb, int messageType) {
    // Message Type
    bb.writeShort((short) messageType);
    byte[] reserved = { (byte) 0, 0, 0 };
    switch(messageType) {
        case H5Constants.NIL_MESSAGE:
            // Data Size
            bb.writeShort((short) 104);
            break;
        case H5Constants.DATA_SPACE_MESSAGE:
            // Data Size
            bb.writeShort((short) 40);
            break;
        case H5Constants.DATA_TYPE_MESSAGE:
            // Data Size
            bb.writeShort((short) 24);
            break;
        case H5Constants.FILL_VALUE_MESSAGE:
            // Data Size
            bb.writeShort((short) 8);
            break;
        case H5Constants.SYMBOL_TABLE_MESSAGE:
            // Data Size
            bb.writeShort((short) 16);
            break;
        case H5Constants.OBJECT_MODIFICATION_TIME_MESSAGE:
            // Data Size
            bb.writeShort((short) 8);
            break;
        case H5Constants.DATA_LAYOUT_MESSAGE:
            // Data Size
            bb.writeShort((short) 24);
            break;
        default:
            throw new H5RuntimeException("Unrecognized message type = " + messageType);
    }
    // Flags
    if (flags.length() != 0) {
        bb.writeBitSet(flags, flags.length());
    } else {
        bb.writeByte(0);
    }
    // Skip 3 reserved zero bytes
    bb.writeBytes(reserved);
}