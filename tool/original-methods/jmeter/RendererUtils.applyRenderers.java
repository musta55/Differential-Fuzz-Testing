public static void applyRenderers(final JTable table, final TableCellRenderer[] renderers) {
    final TableColumnModel columnModel = table.getColumnModel();
    for (int i = 0; i < renderers.length; i++) {
        final TableCellRenderer rend = renderers[i];
        if (rend != null) {
            columnModel.getColumn(i).setCellRenderer(rend);
        }
    }
}