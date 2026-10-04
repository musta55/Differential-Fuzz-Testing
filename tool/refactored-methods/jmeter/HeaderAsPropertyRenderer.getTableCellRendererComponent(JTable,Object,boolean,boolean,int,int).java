@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (table != null) {
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            applyHeaderStyles(header);
        }
        setText(getText(value, row, column));
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(SwingConstants.CENTER);
    }
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void applyHeaderStyles(JTableHeader header) {
    setForeground(header.getForeground());
    setBackground(header.getBackground());
    setFont(header.getFont());
}

