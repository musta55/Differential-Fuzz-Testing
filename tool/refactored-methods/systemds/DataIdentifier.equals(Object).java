@Override
public boolean equals(Object that) {
    if (!(that instanceof DataIdentifier))
        return false;
    DataIdentifier target = (DataIdentifier) that;
    return Objects.equals(getName(), target.getName()) && Objects.equals(getDataType(), target.getDataType()) && Objects.equals(getValueType(), target.getValueType()) && Objects.equals(getFileFormat(), target.getFileFormat()) && this.getDim1() == target.getDim1() && this.getDim2() == target.getDim2();
}