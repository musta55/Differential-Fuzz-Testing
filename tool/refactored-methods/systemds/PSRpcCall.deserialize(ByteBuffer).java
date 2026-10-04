@Override
public void deserialize(ByteBuffer buffer) throws IOException {
    ByteBufferDataInput dis = new ByteBufferDataInput(buffer);
    _method = dis.readInt();
    isValidMethod(_method);
    _workerID = dis.readInt();
    if (dis.available() > 1)
        _data = readAndDeserialize(dis);
}
// ---- helper method(s) introduced by the refactoring ----
private static void isValidMethod(int method) {
    switch(method) {
        case PUSH:
        case PULL:
            break;
        default:
            throw new IllegalArgumentException("PSRpcCall: only support rpc method 'push' or 'pull'");
    }
}

