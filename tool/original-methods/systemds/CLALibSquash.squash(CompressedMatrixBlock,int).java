/**
 * Squash or recompress is process each column group in the given Compressed Matrix Block and tries to recompress
 * each column.
 *
 * @param m The input compressed matrix
 * @param k The parallelization degree allowed in this process
 * @return A replaced Compressed Matrix Block, note the old block is also modified
 */
public static CompressedMatrixBlock squash(CompressedMatrixBlock m, int k) {
    List<AColGroup> before = m.getColGroups();
    List<AColGroup> groups = new ArrayList<>(before.size());
    for (AColGroup g : before) groups.add(g.recompress());
    m.allocateColGroupList(groups);
    return m;
}