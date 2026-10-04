@Override
public void writeDoubleArray(int len, double[] varr) throws IOException {
    for (int i = 0; i < len; ) {
        int lblen = Math.min(len - i, (_bufflen - _count) / 8);
        if (lblen == 0) {
            flushBuffer();
            continue;
        }
        for (int j = 0; j < lblen; j++) {
            writeLong(Double.doubleToRawLongBits(varr[i + j]));
        }
        i += lblen;
    }
}