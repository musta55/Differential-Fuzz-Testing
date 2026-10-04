public H5LocalHeap(H5RootObject rootObject, long address) {
    try {
        this.rootObject = rootObject;
        int sizeOfLength = rootObject.getSuperblock().sizeOfLengths;
        int sizeOfOffset = rootObject.getSuperblock().sizeOfOffsets;
        // Header
        int headerSize = 8 + sizeOfLength + sizeOfLength + sizeOfOffset;
        ByteBuffer header = rootObject.readBufferFromAddress(address, headerSize);
        byte[] formatSignatureBytes = new byte[4];
        header.get(formatSignatureBytes, 0, formatSignatureBytes.length);
        // Verify signature
        if (!Arrays.equals(HEAP_SIGNATURE, formatSignatureBytes)) {
            throw new H5RuntimeException("Heap signature not matched");
        }
        // Version
        rootObject.setLocalHeapVersion(header.get());
        // Move past reserved space
        header.position(8);
        // Data Segment Size
        dataSegmentSize = Utils.readBytesAsUnsignedLong(header, sizeOfLength);
        // Offset to Head of Free-list
        offsetToHeadOfFreeList = Utils.readBytesAsUnsignedLong(header, sizeOfLength);
        // Address of Data Segment
        addressOfDataSegment = Utils.readBytesAsUnsignedLong(header, sizeOfOffset);
        dataBuffer = rootObject.readBufferFromAddress(addressOfDataSegment, (int) dataSegmentSize);
    } catch (Exception e) {
        throw new H5RuntimeException("Error reading local heap", e);
    }
}