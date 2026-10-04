@Override
public void actionPerformed(ActionEvent action) {
    String command = action.getActionCommand();
    if (SYSTEM.equals(command) || JMETER.equals(command)) {
        setUpData();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Properties getSelectedProperties() {
    if (systemButton.isSelected()) {
        return System.getProperties();
    }
    if (jmeterButton.isSelected()) {
        return JMeterUtils.getJMeterProperties();
    }
    return null;
}

private void addPropertiesToTableModel(Properties properties) {
    List<Map.Entry<Object, Object>> entries = new ArrayList<>(properties.entrySet());
    entries.sort(Comparator.comparing(o -> (String) o.getKey()));
    for (Map.Entry<Object, Object> entry : entries) {
        tableModel.addRow(entry);
    }
}

