public H5DataLayoutMessage(H5RootObject rootObject, BitSet flags, ByteBuffer bb) {
    this(rootObject, flags, Utils.readBytesAsUnsignedLong(bb, rootObject.getSuperblock().sizeOfOffsets), Utils.readBytesAsUnsignedLong(bb, rootObject.getSuperblock().sizeOfLengths));
    rootObject.setDataLayoutVersion(bb.get());
    rootObject.setDataLayoutClass(bb.get());
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Data layout (version=" + layoutVersion + ", class=" + layoutClass + ") address=" + address + " size=" + size);
    }
}