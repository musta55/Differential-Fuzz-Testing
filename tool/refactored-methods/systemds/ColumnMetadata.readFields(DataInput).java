@Override
public void readFields(DataInput in) throws IOException {
    _ndistinct = in.readLong();
    _mvValue = "".equals(_mvValue = in.readUTF()) ? null : _mvValue;
}