public H5LocalHeap(H5RootObject rootObject, String childName, long dataSegmentSize, long offsetToHeadOfFreeList, long addressOfDataSegment) {
    this.rootObject = rootObject;
    this.dataSegmentSize = dataSegmentSize;
    int blockCount = calculateBlockCount(childName);
    this.offsetToHeadOfFreeList = calculateOffsetToHeadOfFreeList(blockCount);
    this.addressOfDataSegment = addressOfDataSegment;
    this.dataBuffer = initializeDataBuffer(childName, blockCount);
}
// ---- helper method(s) introduced by the refactoring ----
private ByteBuffer readHeader(long address, int sizeOfLength, int sizeOfOffset) {
    int headerSize = 8 + sizeOfLength + sizeOfLength + sizeOfOffset;
    return rootObject.readBufferFromAddress(address, headerSize);
}

private void validateHeapSignature(ByteBuffer header) {
    byte[] formatSignatureBytes = new byte[4];
    header.get(formatSignatureBytes, 0, formatSignatureBytes.length);
    if (!Arrays.equals(HEAP_SIGNATURE, formatSignatureBytes)) {
        throw new H5RuntimeException("Heap signature not matched");
    }
}

private long parseDataSegmentSize(ByteBuffer header, int sizeOfLength) {
    return Utils.readBytesAsUnsignedLong(header, sizeOfLength);
}

private long parseOffsetToHeadOfFreeList(ByteBuffer header, int sizeOfLength) {
    return Utils.readBytesAsUnsignedLong(header, sizeOfLength);
}

private long parseAddressOfDataSegment(ByteBuffer header, int sizeOfOffset) {
    return Utils.readBytesAsUnsignedLong(header, sizeOfOffset);
}

private int calculateBlockCount(String childName) {
    return (int) (childName.length() / 8f + 1) + 1;
}

private long calculateOffsetToHeadOfFreeList(int blockCount) {
    return blockCount * 8L;
}

private ByteBuffer initializeDataBuffer(String childName, int blockCount) {
    byte[] childNameBytes = childName.getBytes(StandardCharsets.US_ASCII);
    ByteBuffer buffer = ByteBuffer.allocate((int) this.dataSegmentSize);
    buffer.position(8);
    buffer.put(childNameBytes);
    buffer.put(H5Constants.NULL);
    buffer.position(blockCount * 8 - 1);
    buffer.putShort((short) 1);
    buffer.position((int) (this.offsetToHeadOfFreeList + 8 - 1));
    buffer.putShort((short) (this.dataSegmentSize - this.offsetToHeadOfFreeList));
    return buffer;
}

