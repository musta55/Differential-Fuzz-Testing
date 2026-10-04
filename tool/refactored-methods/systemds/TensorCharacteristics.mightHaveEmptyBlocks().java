@Override
public boolean mightHaveEmptyBlocks() {
    return getNonZeros() < getLength();
}