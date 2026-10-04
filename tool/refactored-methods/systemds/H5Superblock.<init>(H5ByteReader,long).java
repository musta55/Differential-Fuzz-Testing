public H5Superblock(H5ByteReader reader, long address) {
    long cursor = address + HDF5_FILE_SIGNATURE_LENGTH;
    try {
        readSuperblockHeader(reader, cursor);
        readAddresses(reader, cursor);
    } catch (Exception e) {
        throw new H5RuntimeException("Failed to read superblock from address " + address, e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void readSuperblockHeader(H5ByteReader reader, long cursor) throws Exception {
    int superBlockHeaderSize = 12;
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
}

private void readAddresses(H5ByteReader reader, long cursor) throws Exception {
    int nextSectionSize = 4 * sizeOfOffsets;
    ByteBuffer header = reader.read(cursor, nextSectionSize);
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
}

private void writeFileSignature(H5BufferBuilder bb) {
    // HDF5 File Signature (8 bytes)
    bb.writeBytes(HDF5_FILE_SIGNATURE);
}

private void writeSuperblockHeader(H5BufferBuilder bb) {
    // Version # of Superblock
    bb.writeByte(versionOfSuperblock);
    // Version # of File Free-space Storage
    bb.writeByte(versionNumberOfTheFileFreeSpaceInformation);
    // Version # of Root Group Symbol Table Entry
    bb.writeByte(versionOfRootGroupSymbolTableEntry);
    // Skip reserved byte
    bb.writeByte(0);
    // Version # of Shared Header Message Format
    bb.writeByte(versionOfSharedHeaderMessageFormat);
    // Size of Offsets
    bb.writeByte(sizeOfOffsets);
    // Size of Lengths
    bb.writeByte(sizeOfLengths);
    // Skip reserved byte
    bb.writeByte(0);
    // Group Leaf Node K
    bb.writeShort((short) groupLeafNodeK);
    // Group Internal Node K
    bb.writeShort((short) groupInternalNodeK);
    // File Consistency Flags (skip)
    bb.writeInt(0);
}

private void writeAddresses(H5BufferBuilder bb) {
    // Base Address
    bb.writeLong(baseAddressByte);
    // Address of Global Free-space Index
    bb.writeLong(addressOfGlobalFreeSpaceIndex);
    // End of File Address
    bb.writeLong(endOfFileAddress);
    // Driver Information Block Address
    bb.writeLong(driverInformationBlockAddress);
}

