// H5 format write/read steps:
// 1. Create/Open a File (H5Fcreate)
// 2. Create/open a Dataspace
// 3. Create/Open a Dataset
// 4. Write/Read
// 5. Close File
public static H5RootObject H5Fopen(H5ByteReader reader) {
    H5RootObject rootObject = new H5RootObject();
    try {
        // Find out if the file is a HDF5 file
        long offset = findValidSignatureOffset(reader);
        if (offset == -1) {
            throw new H5RuntimeException("No valid HDF5 signature found");
        }
        rootObject.setByteReader(reader);
        final H5Superblock superblock = new H5Superblock(reader, offset);
        rootObject.setSuperblock(superblock);
    } catch (Exception exception) {
        throw new H5RuntimeException("Can't open fine " + exception);
    }
    return rootObject;
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

