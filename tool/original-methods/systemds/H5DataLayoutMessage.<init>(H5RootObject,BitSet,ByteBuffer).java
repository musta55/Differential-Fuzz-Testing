public H5DataLayoutMessage(H5RootObject rootObject, BitSet flags, ByteBuffer bb) {
    super(rootObject, flags);
    rootObject.setDataLayoutVersion(bb.get());
    layoutVersion = rootObject.getDataLayoutVersion();
    rootObject.setDataLayoutClass(bb.get());
    layoutClass = rootObject.getDataLayoutClass();
    this.address = Utils.readBytesAsUnsignedLong(bb, rootObject.getSuperblock().sizeOfOffsets);
    this.size = Utils.readBytesAsUnsignedLong(bb, rootObject.getSuperblock().sizeOfLengths);
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Data layout (version=" + layoutVersion + ", class=" + layoutClass + ") address=" + address + " size=" + size);
    }
}