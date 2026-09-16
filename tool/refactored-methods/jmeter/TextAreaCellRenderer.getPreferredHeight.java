public int getPreferredHeight() {
    // Allow override for unit testing only
    // TODO Find a better way
    if (isHeadless()) {
        return 10;
    } else {
        return rend.getPreferredSize().height + 5;
    }
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

