@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.NIL_MESSAGE);
    byte[] reserve = new byte[104];
    bb.writeBytes(reserve);
}