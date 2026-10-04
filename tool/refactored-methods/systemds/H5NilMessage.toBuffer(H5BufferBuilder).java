@Override
public void toBuffer(H5BufferBuilder bb) {
    super.toBuffer(bb, H5Constants.NIL_MESSAGE);
    writeReserveBytes(bb);
}
// ---- helper method(s) introduced by the refactoring ----
private void writeReserveBytes(H5BufferBuilder bb) {
    byte[] reserve = new byte[104];
    bb.writeBytes(reserve);
}

