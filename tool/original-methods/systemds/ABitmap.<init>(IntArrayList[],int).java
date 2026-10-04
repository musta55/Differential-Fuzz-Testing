/**
 * Main constructor of bitMap, it should be guaranteed that the offsetLists are not null.
 *
 * @param offsetsLists The offsets to the values
 * @param rows         The number of rows encoded
 */
protected ABitmap(IntArrayList[] offsetsLists, int rows) {
    int offsetsTotal = 0;
    for (IntArrayList a : offsetsLists) offsetsTotal += a.size();
    _numZeros = rows - offsetsTotal;
    _offsetsLists = offsetsLists;
}