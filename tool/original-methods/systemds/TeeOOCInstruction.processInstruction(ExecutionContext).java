public void processInstruction(ExecutionContext ec) {
    //get input stream
    MatrixObject min = ec.getMatrixObject(input1);
    OOCStreamable<IndexedMatrixValue> streamable = min.getStreamable();
    CachingStream handle;
    if (streamable.hasStreamCache()) {
        handle = streamable.getStreamCache();
        incrRef(handle, 1);
    } else {
        // We also set the input stream handle
        handle = new CachingStream(min.getStreamHandle());
        min.setStreamHandle(handle);
        incrRef(handle, 2);
    }
    //get output and create new resettable stream
    MatrixObject mo = ec.getMatrixObject(output);
    mo.setStreamHandle(handle);
    mo.setMetaData(min.getMetaData());
}