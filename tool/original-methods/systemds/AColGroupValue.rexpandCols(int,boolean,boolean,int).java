@Override
public AColGroup rexpandCols(int max, boolean ignore, boolean cast, int nRows) {
    IDictionary d = _dict.rexpandCols(max, ignore, cast, _colIndexes.size());
    if (d == null) {
        if (max <= 0)
            return null;
        return ColGroupEmpty.create(max);
    } else {
        IColIndex outCols = ColIndexFactory.create(d.getNumberOfColumns(_dict.getNumberOfValues(1)));
        return copyAndSet(outCols, d);
    }
}