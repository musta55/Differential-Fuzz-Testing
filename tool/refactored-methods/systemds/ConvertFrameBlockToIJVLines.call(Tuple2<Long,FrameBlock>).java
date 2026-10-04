@Override
public Iterator<String> call(Tuple2<Long, FrameBlock> kv) throws Exception {
    long rowoffset = kv._1;
    FrameBlock block = kv._2;
    ArrayList<String> cells = new ArrayList<>();
    addMetadata(cells, rowoffset, block);
    addIJVLines(cells, rowoffset, block);
    return cells.iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private void addMetadata(ArrayList<String> cells, long rowoffset, FrameBlock block) {
    if (rowoffset == 1) {
        for (int j = 0; j < block.getNumColumns(); j++) {
            if (!block.isColumnMetadataDefault(j)) {
                cells.add("-1 " + (j + 1) + " " + block.getColumnMetadata(j).getNumDistinct());
                cells.add("-2 " + (j + 1) + " " + block.getColumnMetadata(j).getMvValue());
            }
        }
    }
}

private void addIJVLines(ArrayList<String> cells, long rowoffset, FrameBlock block) {
    StringBuilder sb = new StringBuilder();
    Iterator<String[]> iter = IteratorFactory.getStringRowIterator(block);
    for (int i = 0; iter.hasNext(); i++) {
        // for all rows
        String rowIndex = Long.toString(rowoffset + i);
        String[] row = iter.next();
        for (int j = 0; j < row.length; j++) {
            if (row[j] != null) {
                sb.append(rowIndex);
                sb.append(' ');
                sb.append(j + 1);
                sb.append(' ');
                sb.append(row[j]);
                cells.add(sb.toString());
                sb.setLength(0);
            }
        }
    }
}

