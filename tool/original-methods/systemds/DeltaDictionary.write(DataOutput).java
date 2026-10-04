@Override
public void write(DataOutput out) throws IOException {
    out.writeByte(DictionaryFactory.Type.DELTA_DICT.ordinal());
    out.writeInt(_numCols);
    out.writeInt(_values.length);
    for (int i = 0; i < _values.length; i++) out.writeDouble(_values[i]);
}