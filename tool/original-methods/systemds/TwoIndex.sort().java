@Override
public IColIndex sort() {
    if (id2 < id1)
        return new TwoIndex(id2, id1);
    else
        return this;
}