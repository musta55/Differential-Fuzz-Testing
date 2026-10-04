public Encrypted(int[] dims, DataCharacteristics dc, byte[] data, Types.DataType dt) {
    super(dt, Types.ValueType.UNKNOWN);
    _dims = dims;
    _dc = dc;
    _data = data;
}