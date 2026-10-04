@Override
public void writeDoubleArray(int len, double[] varr) throws IOException {
    for (int i = 0; i < len; ) {
        if (_count >= _bufflen)
            flushBuffer();
        int lblen = Math.min(len - i, (_bufflen - _count) / 8);
        if (lblen == 0) {
            flushBuffer();
            continue;
        }
        for (int j = 0; j < lblen; j++) {
            longToBa(Double.doubleToRawLongBits(varr[i + j]), _buff, _count);
            _count += 8;
        }
        _position += 8L * lblen;
        i += lblen;
        if (_count >= _bufflen)
            flushBuffer();
    }
}