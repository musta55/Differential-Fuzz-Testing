@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (delegate instanceof DefaultTableCellRenderer) {
        DefaultTableCellRenderer tr = (DefaultTableCellRenderer) delegate;
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
    return delegate.getTableCellRendererComponent(table, HeaderAsPropertyRenderer.getText(value, row, column, null), isSelected, hasFocus, row, column);
}