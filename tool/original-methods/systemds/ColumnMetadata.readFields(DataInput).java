@Override
public void readFields(DataInput in) throws IOException {
    _ndistinct = in.readLong();
    _mvValue = in.readUTF();
    if (_mvValue.equals(""))
        _mvValue = null;
}