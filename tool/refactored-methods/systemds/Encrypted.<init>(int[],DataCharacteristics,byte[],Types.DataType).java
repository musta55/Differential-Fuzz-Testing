public Encrypted(int[] dims, DataCharacteristics dc, byte[] data, Types.DataType dt) {
    super(dt, Types.ValueType.UNKNOWN);
    this._dims = dims;
    this._dc = dc;
    this._data = data;
}