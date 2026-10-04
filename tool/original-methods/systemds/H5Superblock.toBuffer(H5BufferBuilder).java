public void toBuffer(H5BufferBuilder bb) {
    // HDF5 File Signature (8 bytes)
    bb.writeBytes(HDF5_FILE_SIGNATURE);
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
    // Base Address
    bb.writeLong(baseAddressByte);
    // Address of Global Free-space Index
    bb.writeLong(addressOfGlobalFreeSpaceIndex);
    // End of File Address
    bb.writeLong(endOfFileAddress);
    // Driver Information Block Address
    bb.writeLong(driverInformationBlockAddress);
}