public H5BTree(H5RootObject rootObject, long address) {
    this.address = address;
    this.rootObject = rootObject;
    readHeaderAndValidateSignature(rootObject, address);
    int headerSize = calculateHeaderSize();
    ByteBuffer header = rootObject.readBufferFromAddress(address + 6, headerSize);
    this.entriesUsed = Utils.readBytesAsUnsignedInt(header, 2);
    this.leftSiblingAddress = Utils.readBytesAsUnsignedLong(header, rootObject.getSuperblock().sizeOfOffsets);
    this.rightSiblingAddress = Utils.readBytesAsUnsignedLong(header, rootObject.getSuperblock().sizeOfOffsets);
    this.childAddresses = readChildAddresses(address);
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateHeaderSize() {
    return 8 * rootObject.getSuperblock().sizeOfOffsets;
}

private List<Long> readChildAddresses(long address) {
    final int keyBytes = (2 * entriesUsed + 1) * rootObject.getSuperblock().sizeOfLengths;
    final int childPointerBytes = (2 * entriesUsed) * rootObject.getSuperblock().sizeOfLengths;
    final int keysAndPointersBytes = keyBytes + childPointerBytes;
    final long keysAddress = address + 8L + 2L * rootObject.getSuperblock().sizeOfOffsets;
    final ByteBuffer keysAndPointersBuffer = rootObject.readBufferFromAddress(keysAddress, keysAndPointersBytes);
    List<Long> childAddresses = new ArrayList<>(entriesUsed);
    for (int i = 0; i < entriesUsed; i++) {
        keysAndPointersBuffer.position(keysAndPointersBuffer.position() + rootObject.getSuperblock().sizeOfLengths);
        childAddresses.add(Utils.readBytesAsUnsignedLong(keysAndPointersBuffer, rootObject.getSuperblock().sizeOfLengths));
    }
    return childAddresses;
}

