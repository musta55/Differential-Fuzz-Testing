/**
 * Applies the specified cell renderers to the columns of the given table.
 *
 * @param table     the table to which renderers will be applied
 * @param renderers an array of cell renderers to apply to the table columns
 */
public static void applyRenderers(final JTable table, final TableCellRenderer[] renderers) {
    final TableColumnModel columnModel = table.getColumnModel();
    // Iterate over each renderer and apply it to the corresponding column if it is not null
    for (int columnIndex = 0; columnIndex < renderers.length; columnIndex++) {
        final TableCellRenderer renderer = renderers[columnIndex];
        if (renderer != null) {
            columnModel.getColumn(columnIndex).setCellRenderer(renderer);
        }
    }
}