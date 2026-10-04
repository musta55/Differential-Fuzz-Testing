@Override
public ByteBuffer serialize() throws IOException {
    int len = 4 + (_status == Type.SUCCESS ? getExactSerializedSize((ListObject) _data) : _status == Type.SUCCESS_EMPTY ? 0 : IOUtilFunctions.getUTFSize((String) _data));
    CacheDataOutput dos = new CacheDataOutput(len);
    dos.writeInt(_status.ordinal());
    switch(_status) {
        case SUCCESS:
            serializeAndWriteListObject((ListObject) _data, dos);
            break;
        case SUCCESS_EMPTY:
            break;
        case ERROR:
            dos.writeUTF(_data.toString());
            break;
    }
    return ByteBuffer.wrap(dos.getBytes());
}