public H5LocalHeap(H5RootObject rootObject, String childName, long dataSegmentSize, long offsetToHeadOfFreeList, long addressOfDataSegment) {
    this.rootObject = rootObject;
    this.dataSegmentSize = dataSegmentSize;
    int blockCount = (int) (childName.length() / 8f + 1) + 1;
    //offsetToHeadOfFreeList;
    this.offsetToHeadOfFreeList = blockCount * 8L;
    this.addressOfDataSegment = addressOfDataSegment;
    byte[] childName_atBytes = childName.getBytes(StandardCharsets.US_ASCII);
    this.dataBuffer = ByteBuffer.allocate((int) this.dataSegmentSize);
    this.dataBuffer.position(8);
    this.dataBuffer.put(childName_atBytes);
    this.dataBuffer.put(H5Constants.NULL);
    this.dataBuffer.position(blockCount * 8 - 1);
    this.dataBuffer.putShort((short) 1);
    this.dataBuffer.position((int) (this.offsetToHeadOfFreeList + 8 - 1));
    this.dataBuffer.putShort((short) (this.dataSegmentSize - this.offsetToHeadOfFreeList));
}