private void setUpData() {
    tableModel.clearData();
    Properties p = null;
    if (systemButton.isSelected()) {
        p = System.getProperties();
    }
    if (jmeterButton.isSelected()) {
        p = JMeterUtils.getJMeterProperties();
    }
    if (p == null) {
        return;
    }
    Set<Map.Entry<Object, Object>> s = p.entrySet();
    List<Map.Entry<Object, Object>> al = new ArrayList<>(s);
    al.sort(Comparator.comparing(o -> (String) o.getKey()));
    for (Map.Entry<Object, Object> row : al) {
        tableModel.addRow(row);
    }
}