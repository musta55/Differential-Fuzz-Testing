public static void H5Dwrite(H5RootObject rootObject, double[][] data) {
    for (double[] row : data) {
        H5Dwrite(rootObject, row);
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

