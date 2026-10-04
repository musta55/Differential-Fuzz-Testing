// Create Data Space
public static H5RootObject H5Screate(BufferedOutputStream bos, long row, long col) {
    try {
        H5RootObject rootObject = new H5RootObject();
        rootObject.setBufferedOutputStream(bos);
        rootObject.bufferBuilder = new H5BufferBuilder();
        final H5Superblock superblock = createSuperblock(row, col);
        rootObject.setSuperblock(superblock);
        rootObject.setRank(2);
        rootObject.setCol(col);
        rootObject.setRow(row);
        superblock.toBuffer(rootObject.bufferBuilder);
        H5SymbolTableEntry symbolTableEntry = new H5SymbolTableEntry(rootObject);
        symbolTableEntry.toBuffer(rootObject.bufferBuilder);
        return rootObject;
    } catch (Exception exception) {
        throw new H5RuntimeException(exception);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static long findValidSignatureOffset(H5ByteReader reader) {
    int maxSignatureLength = 2048;
    for (long offset = 0; offset < maxSignatureLength; offset = nextOffset(offset)) {
        if (H5Superblock.verifySignature(reader, offset)) {
            return offset;
        }
    }
    return -1;
}

private static H5Superblock createSuperblock(long row, long col) {
    H5Superblock superblock = new H5Superblock();
    superblock.versionOfSuperblock = 0;
    superblock.versionNumberOfTheFileFreeSpaceInformation = 0;
    superblock.versionOfRootGroupSymbolTableEntry = 0;
    superblock.versionOfSharedHeaderMessageFormat = 0;
    superblock.sizeOfOffsets = 8;
    superblock.sizeOfLengths = 8;
    superblock.groupLeafNodeK = 4;
    superblock.groupInternalNodeK = 16;
    superblock.baseAddressByte = 0;
    superblock.addressOfGlobalFreeSpaceIndex = -1;
    // double value
    superblock.endOfFileAddress = 2048 + (row * col * 8);
    superblock.driverInformationBlockAddress = -1;
    superblock.rootGroupSymbolTableAddress = 56;
    return superblock;
}

