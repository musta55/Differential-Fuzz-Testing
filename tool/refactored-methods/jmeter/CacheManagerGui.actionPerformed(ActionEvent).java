@Override
public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (CONTROLLED_BY_THREADGROUP.equals(action)) {
        clearEachIteration.setEnabled(!controlledByThreadGroup.isSelected());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static JCheckBox createCheckBox(String resourceKey, boolean selected) {
    return new JCheckBox(JMeterUtils.getResString(resourceKey), selected);
}

