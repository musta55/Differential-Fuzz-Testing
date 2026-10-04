protected MaterializeSort(int endLength, int numRows, IntArrayList[] offsets) {
    super(endLength, numRows, offsets);
    // + 1 to ensure that the _numLabels is possible to represent in the map.
    md = MapToFactory.create(Math.min(_numRows, CACHE_BLOCK), _numLabels + 1);
    skip = new int[offsets.length];
    for (int block = 0; block < _numRows; block += CACHE_BLOCK) insert(block, Math.min(block + CACHE_BLOCK, _numRows));
}