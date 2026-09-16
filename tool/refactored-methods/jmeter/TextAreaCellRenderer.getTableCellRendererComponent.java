@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    //$NON-NLS-1$
    rend = createRenderer(value == null ? "" : (String) value);
    applyColors(hasFocus, isSelected);
    adjustRowHeight(table, row);
    return JTextScrollPane.getInstance(rend);
}
// ---- helper method(s) introduced by the refactoring ----
private void applyColors(boolean hasFocus, boolean isSelected) {
    if (hasFocus || isSelected) {
        rend.setBackground(Color.blue);
        rend.setForeground(Color.white);
    } else {
        rend.setBackground(null);
        rend.setForeground(null);
    }
}

private void adjustRowHeight(JTable table, int row) {
    if (table.getRowHeight(row) < getPreferredHeight()) {
        table.setRowHeight(row, getPreferredHeight());
    }
}

private static boolean isHeadless() {
    // $NON-NLS-1$ $NON-NLS-2$
    return "true".equals(System.getProperty("java.awt.headless"));
}

