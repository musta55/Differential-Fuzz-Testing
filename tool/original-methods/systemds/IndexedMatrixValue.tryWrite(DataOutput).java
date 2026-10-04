@Override
public boolean tryWrite(DataOutput dataOutput) throws IOException {
    MatrixIndexes ix = _indexes;
    MatrixValue value = _value;
    if (ix == null || value == null)
        return false;
    ix.write(dataOutput);
    value.write(dataOutput);
    return true;
}