public H5DataSpaceMessage(H5RootObject rootObject, BitSet flags, ByteBuffer bb) {
    super(rootObject, flags);
    rootObject.setDataSpaceVersion(bb.get());
    rootObject.setRank(bb.get());
    byte[] flagBits = new byte[1];
    bb.get(flagBits);
    BitSet maxFlags = BitSet.valueOf(flagBits);
    maxSizesPresent = maxFlags.get(0);
    // Skip 5 reserved bytes
    bb.position(bb.position() + 5);
    // Dimensions sizes
    if (rootObject.getRank() != 0) {
        int[] dimensions = new int[rootObject.getRank()];
        for (int i = 0; i < rootObject.getRank(); i++) {
            dimensions[i] = Utils.readBytesAsUnsignedInt(bb, rootObject.getSuperblock().sizeOfLengths);
        }
        rootObject.setDimensions(dimensions);
    } else {
        rootObject.setDimensions(new int[0]);
    }
    // Max dimension sizes
    if (maxSizesPresent) {
        int[] maxSizes = new int[rootObject.getRank()];
        for (int i = 0; i < rootObject.getRank(); i++) {
            maxSizes[i] = Utils.readBytesAsUnsignedInt(bb, rootObject.getSuperblock().sizeOfLengths);
        }
        rootObject.setMaxSizes(maxSizes);
    } else {
        rootObject.setMaxSizes(new int[0]);
    }
    // Calculate the total length by multiplying all dimensions
    totalLength = IntStream.of(rootObject.getLogicalDimensions()).mapToLong(Long::valueOf).reduce(1, Math::multiplyExact);
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Dataspace rank=" + rootObject.getRank() + " dims=" + Arrays.toString(rootObject.getLogicalDimensions()) + " => rows=" + rootObject.getRow() + ", cols(flat)=" + rootObject.getCol());
    }
}