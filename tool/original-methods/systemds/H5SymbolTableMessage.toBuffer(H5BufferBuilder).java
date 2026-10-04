@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.SYMBOL_TABLE_MESSAGE);
    // Write values
    bb.writeLong(this.bTreeAddress);
    bb.writeLong(this.localHeapAddress);
}