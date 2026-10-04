@Override
public boolean equals(Object that) {
    if (!(that instanceof DataIdentifier))
        return false;
    DataIdentifier target = (DataIdentifier) that;
    if (getName() != null && !getName().equals(target.getName()))
        return false;
    if (getDataType() != null && !getDataType().equals(target.getDataType()))
        return false;
    if (getValueType() != null && !getValueType().equals(target.getValueType()))
        return false;
    if (getFileFormat() != null && getFileFormat() != target.getFileFormat())
        return false;
    if (!(this.getDim1() == target.getDim1()))
        return false;
    if (!(this.getDim2() == target.getDim2()))
        return false;
    return true;
}