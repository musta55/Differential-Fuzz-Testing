public void clearData() {
    String[] headers = model.getHeaders();
    model = new Data();
    model.setHeaders(headers);
    this.fireTableDataChanged();
}