@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.DATA_SPACE_MESSAGE);
    writeHeader(bb);
    writeDimensions(bb);
    writeMaxSizes(bb);
}
// ---- helper method(s) introduced by the refactoring ----
private void readHeader(ByteBuffer bb) {
    rootObject.setDataSpaceVersion(bb.get());
    rootObject.setRank(bb.get());
    BitSet maxFlags = BitSet.valueOf(new byte[] { bb.get() });
    maxSizesPresent = maxFlags.get(0);
    // Skip 5 reserved bytes
    bb.position(bb.position() + 5);
}

private void readDimensions(ByteBuffer bb) {
    if (rootObject.getRank() != 0) {
        int[] dimensions = new int[rootObject.getRank()];
        for (int i = 0; i < rootObject.getRank(); i++) {
            dimensions[i] = Utils.readBytesAsUnsignedInt(bb, rootObject.getSuperblock().sizeOfLengths);
        }
        rootObject.setDimensions(dimensions);
    } else {
        rootObject.setDimensions(new int[0]);
    }
}

private void readMaxSizes(ByteBuffer bb) {
    if (maxSizesPresent) {
        int[] maxSizes = new int[rootObject.getRank()];
        for (int i = 0; i < rootObject.getRank(); i++) {
            maxSizes[i] = Utils.readBytesAsUnsignedInt(bb, rootObject.getSuperblock().sizeOfLengths);
        }
        rootObject.setMaxSizes(maxSizes);
    } else {
        rootObject.setMaxSizes(new int[0]);
    }
}

private void calculateTotalLength() {
    totalLength = IntStream.of(rootObject.getLogicalDimensions()).mapToLong(Long::valueOf).reduce(1, Math::multiplyExact);
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Dataspace rank=" + rootObject.getRank() + " dims=" + Arrays.toString(rootObject.getLogicalDimensions()) + " => rows=" + rootObject.getRow() + ", cols(flat)=" + rootObject.getCol());
    }
}

private void writeHeader(H5BufferBuilder bb) {
    bb.writeByte(rootObject.getDataSpaceVersion());
    bb.writeByte(rootObject.getRank());
    byte flag = maxSizesPresent ? (byte) 1 : (byte) 0;
    bb.writeByte(flag);
    // Skip 5 reserved bytes
    bb.writeBytes(new byte[5]);
}

private void writeDimensions(H5BufferBuilder bb) {
    if (rootObject.getRank() != 0) {
        for (int i = 0; i < rootObject.getRank(); i++) {
            bb.write(rootObject.getLogicalDimensions()[i], rootObject.getSuperblock().sizeOfLengths);
        }
    }
}

private void writeMaxSizes(H5BufferBuilder bb) {
    if (maxSizesPresent) {
        for (int i = 0; i < rootObject.getRank(); i++) {
            bb.write(rootObject.getMaxSizes()[i], rootObject.getSuperblock().sizeOfLengths);
        }
    }
}

