@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (delegate instanceof DefaultTableCellRenderer) {
        DefaultTableCellRenderer tr = (DefaultTableCellRenderer) delegate;
        setupHeaderProperties(tr, table);
    }
    return delegate.getTableCellRendererComponent(table, HeaderAsPropertyRenderer.getText(value, row, column, null), isSelected, hasFocus, row, column);
}
// ---- helper method(s) introduced by the refactoring ----
private static void setupHeaderProperties(DefaultTableCellRenderer tr, JTable table) {
    if (table != null) {
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            tr.setForeground(header.getForeground());
            tr.setBackground(header.getBackground());
            tr.setFont(header.getFont());
        }
    }
    tr.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    tr.setHorizontalAlignment(SwingConstants.CENTER);
}

