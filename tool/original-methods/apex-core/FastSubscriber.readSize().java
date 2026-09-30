@Override
public int readSize() {
    if (writeOffset - readOffset < 2) {
        return -1;
    }
    short s = buffer[readOffset++];
    return s | (buffer[readOffset++] << 8);
}