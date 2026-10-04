public H5Superblock(H5ByteReader reader, long address) {
    // Calculated bytes for the super block header is = 56
    int superBlockHeaderSize = 12;
    long cursor = address + HDF5_FILE_SIGNATURE_LENGTH;
    try {
        ByteBuffer header = reader.read(cursor, superBlockHeaderSize);
        header.order(LITTLE_ENDIAN);
        header.rewind();
        cursor += superBlockHeaderSize;
        // Version # of Superblock
        versionOfSuperblock = header.get();
        if (versionOfSuperblock != 0 && versionOfSuperblock != 1) {
            throw new H5RuntimeException("Detected superblock version not 0 or 1");
        }
        // Version # of File Free-space Storage
        versionNumberOfTheFileFreeSpaceInformation = header.get();
        // Version # of Root Group Symbol Table Entry
        versionOfRootGroupSymbolTableEntry = header.get();
        // Skip reserved byte
        header.position(header.position() + 1);
        // Version # of Shared Header Message Format
        versionOfSharedHeaderMessageFormat = header.get();
        // Size of Offsets
        sizeOfOffsets = Byte.toUnsignedInt(header.get());
        // Size of Lengths
        sizeOfLengths = Byte.toUnsignedInt(header.get());
        // Skip reserved byte
        header.position(header.position() + 1);
        // Group Leaf Node K
        groupLeafNodeK = Short.toUnsignedInt(header.getShort());
        // Group Internal Node K
        groupInternalNodeK = Short.toUnsignedInt(header.getShort());
        // File Consistency Flags (skip)
        cursor += 4;
        int nextSectionSize = 4 * sizeOfOffsets;
        header = reader.read(cursor, nextSectionSize);
        header.order(LITTLE_ENDIAN);
        header.rewind();
        cursor += nextSectionSize;
        // Base Address
        baseAddressByte = Utils.readBytesAsUnsignedLong(header, sizeOfOffsets);
        // Address of Global Free-space Index
        addressOfGlobalFreeSpaceIndex = Utils.readBytesAsUnsignedLong(header, sizeOfOffsets);
        // End of File Address
        endOfFileAddress = Utils.readBytesAsUnsignedLong(header, sizeOfOffsets);
        // Driver Information Block Address
        driverInformationBlockAddress = Utils.readBytesAsUnsignedLong(header, sizeOfOffsets);
        // Root Group Symbol Table Entry Address
        rootGroupSymbolTableAddress = cursor;
    } catch (Exception e) {
        throw new H5RuntimeException("Failed to read superblock from address " + address, e);
    }
}