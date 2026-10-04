@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.DATA_SPACE_MESSAGE);
    bb.writeByte(rootObject.getDataSpaceVersion());
    bb.writeByte(rootObject.getRank());
    byte flag = 0;
    if (rootObject.getMaxSizes() != null && rootObject.getMaxSizes().length > 0) {
        flag = 1;
    }
    bb.writeByte(flag);
    // Skip 5 reserved bytes
    byte[] reserved = new byte[5];
    bb.writeBytes(reserved);
    // Dimensions sizes
    if (rootObject.getRank() != 0) {
        for (int i = 0; i < rootObject.getRank(); i++) {
            bb.write(rootObject.getLogicalDimensions()[i], rootObject.getSuperblock().sizeOfLengths);
        }
    }
    // Max dimension sizes
    if (flag == 1) {
        for (int i = 0; i < rootObject.getRank(); i++) {
            bb.write(rootObject.getMaxSizes()[i], rootObject.getSuperblock().sizeOfLengths);
        }
    }
}