@Override
public void keyReleased(KeyEvent e) {
    String fieldName = e.getComponent().getName();
    if (WARNING_THRESHOLD_FIELD.equals(fieldName)) {
        validateInteger(warningThresholdField);
    } else if (ERROR_THRESHOLD_FIELD.equals(fieldName)) {
        validateInteger(errorThresholdField);
    }
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

