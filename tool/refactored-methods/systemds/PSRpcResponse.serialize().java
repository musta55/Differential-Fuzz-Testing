@Override
public ByteBuffer serialize() throws IOException {
    int len = calculateSerializedLength();
    CacheDataOutput dos = new CacheDataOutput(len);
    dos.writeInt(_status.ordinal());
    writeData(dos);
    return ByteBuffer.wrap(dos.getBytes());
}
// ---- helper method(s) introduced by the refactoring ----
private void readData(ByteBufferDataInput dis) throws IOException {
    switch(_status) {
        case SUCCESS:
            _data = readAndDeserialize(dis);
            break;
        case SUCCESS_EMPTY:
            // No data to read
            break;
        case ERROR:
            _data = dis.readUTF();
            break;
    }
}

private int calculateSerializedLength() {
    return 4 + (_status == Type.SUCCESS ? getExactSerializedSize((ListObject) _data) : _status == Type.SUCCESS_EMPTY ? 0 : IOUtilFunctions.getUTFSize((String) _data));
}

private void writeData(CacheDataOutput dos) throws IOException {
    switch(_status) {
        case SUCCESS:
            serializeAndWriteListObject((ListObject) _data, dos);
            break;
        case SUCCESS_EMPTY:
            // No data to write
            break;
        case ERROR:
            dos.writeUTF(_data.toString());
            break;
    }
}

