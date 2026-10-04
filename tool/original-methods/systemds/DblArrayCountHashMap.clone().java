@Override
public DblArrayCountHashMap clone() {
    DblArrayCountHashMap ret = new DblArrayCountHashMap(size);
    for (ACount<DblArray> e : data) ret.appendValue(e);
    ret.size = size;
    return ret;
}