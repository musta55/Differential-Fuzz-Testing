/**
 * This method is called from errors-only checkbox
 *
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
@Override
public void actionPerformed(ActionEvent e) {
    toggleWarningThresholdField(!errorsOnly.isSelected());
}
// ---- helper method(s) introduced by the refactoring ----
private static long parseLong(String value, long defaultValue) {
    try {
        return Long.parseLong(value);
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}

private void toggleWarningThresholdField(boolean enabled) {
    warningThresholdField.setEnabled(enabled);
    warningThresholdField.setEditable(enabled);
}

