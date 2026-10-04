@Override
public IColIndex sort() {
    return id2 < id1 ? new TwoIndex(id2, id1) : this;
}