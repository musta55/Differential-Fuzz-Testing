public DenseBlockBool(int[] dims, boolean[] data) {
    super(dims);
    _data = new BitSet(data.length);
    for (int i = 0; i < data.length; i++) if (data[i])
        _data.set(i);
}