@Override
public boolean containsAny(IColIndex idx) {
    return idx instanceof SingleIndex ? this.idx == idx.get(0) : idx.contains(this.idx);
}