public AInsertionSorter(int endLength, int numRows, IntArrayList[] offsets) {
    _indexes = new int[endLength];
    _numLabels = offsets.length;
    _labels = MapToFactory.create(endLength, _numLabels);
    _numRows = numRows;
    _offsets = offsets;
    _negativeIndex = -1;
}