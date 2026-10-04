protected AColGroupOffset(IColIndex colIndices, int numRows, boolean zeros, IDictionary dict, int[] ptr, char[] data, int[] cachedCounts) {
    super(colIndices, dict, cachedCounts);
    _numRows = numRows;
    _zeros = zeros;
    _ptr = ptr;
    _data = data;
}