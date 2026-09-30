public FastClassIndexReader(final InputStream is) throws IOException {
    readIntoBuffer(is);
    readConstantPool();
    readIndex();
}