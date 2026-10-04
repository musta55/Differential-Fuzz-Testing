@Override
public void deserialize(ByteBuffer buffer) throws IOException {
    ByteBufferDataInput dis = new ByteBufferDataInput(buffer);
    _method = dis.readInt();
    validateMethod(_method);
    _workerID = dis.readInt();
    if (dis.available() > 1)
        _data = readAndDeserialize(dis);
}