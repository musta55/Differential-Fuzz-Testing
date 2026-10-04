@Override
public void deserialize(ByteBuffer buffer) throws IOException {
    ByteBufferDataInput dis = new ByteBufferDataInput(buffer);
    _status = Type.values()[dis.readInt()];
    switch(_status) {
        case SUCCESS:
            _data = readAndDeserialize(dis);
            break;
        case SUCCESS_EMPTY:
            break;
        case ERROR:
            _data = dis.readUTF();
            break;
    }
}