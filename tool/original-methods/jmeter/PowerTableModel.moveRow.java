@Override
public void moveRow(int start, int end, int to) {
    ArrayList<Object[]> rows = new ArrayList<>();
    for (int i = 0; i < getRowCount(); i++) {
        rows.add(getRowData(i));
    }
    List<Object[]> subList = new ArrayList<>(rows.subList(start, end));
    for (int x = end - 1; x >= start; x--) {
        rows.remove(x);
    }
    rows.addAll(to, subList);
    for (int i = 0; i < rows.size(); i++) {
        setRowValues(i, rows.get(i));
    }
    super.fireTableChanged(new TableModelEvent(this));
}