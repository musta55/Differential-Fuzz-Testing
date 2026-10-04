/**
 * Set array of counts regarding col group types.
 *
 * The position corresponds with the enum ordinal.
 *
 * @param colGroups list of ColGroups used in compression.
 */
protected void setColGroupsCounts(List<AColGroup> colGroups) {
    HashMap<String, int[]> ret = new HashMap<>();
    for (AColGroup c : colGroups) {
        String ct = c.getClass().getSimpleName();
        int colCount = c.getNumCols();
        int[] values;
        if (ret.containsKey(ct)) {
            values = ret.get(ct);
            values[0] += 1;
            values[1] += colCount;
        } else {
            values = new int[] { 1, colCount };
        }
        ret.put(ct, values);
    }
    this.colGroupCounts = ret;
}