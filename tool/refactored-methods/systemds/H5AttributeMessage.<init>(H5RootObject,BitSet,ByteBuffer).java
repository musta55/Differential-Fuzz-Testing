public H5AttributeMessage(H5RootObject rootObject, BitSet flags, ByteBuffer bb) {
    super(rootObject, flags);
    if (bb.remaining() == 0)
        return;
    byte version = bb.get();
    logSkippedAttribute(version, bb.remaining());
    // consume the rest of the payload
    bb.position(bb.limit());
}
// ---- helper method(s) introduced by the refactoring ----
private void logSkippedAttribute(byte version, int remainingBytes) {
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Skipping attribute message v" + version + " (" + remainingBytes + " bytes payload)");
    }
}

