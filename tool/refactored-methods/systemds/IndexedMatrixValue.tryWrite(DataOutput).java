@Override
public boolean tryWrite(DataOutput dataOutput) throws IOException {
    if (_indexes == null || _value == null) {
        return false;
    }
    _indexes.write(dataOutput);
    _value.write(dataOutput);
    return true;
}