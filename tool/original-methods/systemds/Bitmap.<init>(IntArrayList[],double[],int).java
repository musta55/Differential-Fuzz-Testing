/**
 * Single column version of a bitmap.
 *
 * it should be guaranteed that the offsetLists are not null.
 *
 * @param offsetsLists The offsets for the values
 * @param values       The values matched with the offsets
 * @param rows         The number of rows encoded
 */
protected Bitmap(IntArrayList[] offsetsLists, double[] values, int rows) {
    super(offsetsLists, rows);
    _values = values;
}