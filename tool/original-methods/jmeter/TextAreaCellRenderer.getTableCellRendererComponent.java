@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value != null) {
        rend = createRenderer((String) value);
    } else {
        //$NON-NLS-1$
        rend = createRenderer("");
    }
    if (hasFocus || isSelected) {
        rend.setBackground(Color.blue);
        rend.setForeground(Color.white);
    }
    if (table.getRowHeight(row) < getPreferredHeight()) {
        table.setRowHeight(row, getPreferredHeight());
    }
    return JTextScrollPane.getInstance(rend);
}