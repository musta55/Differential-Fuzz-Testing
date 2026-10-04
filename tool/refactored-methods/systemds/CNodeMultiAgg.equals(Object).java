@Override
public boolean equals(Object o) {
    if (!(o instanceof CNodeMultiAgg)) {
        return false;
    }
    CNodeMultiAgg that = (CNodeMultiAgg) o;
    return super.equals(o) && CollectionUtils.isEqualCollection(_aggOps, that._aggOps) && equalInputReferences(_outputs, that._outputs, _inputs, that._inputs);
}