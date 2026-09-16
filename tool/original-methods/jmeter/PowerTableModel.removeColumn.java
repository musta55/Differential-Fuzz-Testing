public void removeColumn(int col) {
    model.removeColumn(col);
    this.fireTableStructureChanged();
}