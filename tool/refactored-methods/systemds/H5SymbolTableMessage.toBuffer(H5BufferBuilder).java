@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.SYMBOL_TABLE_MESSAGE);
    bb.writeLong(bTreeAddress);
    bb.writeLong(localHeapAddress);
}