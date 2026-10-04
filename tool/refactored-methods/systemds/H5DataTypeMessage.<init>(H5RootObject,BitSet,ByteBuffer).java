public H5DataTypeMessage(H5RootObject rootObject, BitSet flags, ByteBuffer bb) {
    this(rootObject, flags, new H5DoubleDataType(bb));
    if (H5RootObject.HDF5_DEBUG) {
        System.out.println("[HDF5] Datatype parsed (class=" + doubleDataType.getDataClass() + ", size=" + doubleDataType.getSize() + ")");
    }
}