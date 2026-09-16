@Override
public void keyReleased(KeyEvent e) {
    String fieldName = e.getComponent().getName();
    if (fieldName.equals(WARNING_THRESHOLD_FIELD)) {
        validateInteger(warningThresholdField);
    }
    if (fieldName.equals(ERROR_THRESHOLD_FIELD)) {
        validateInteger(errorThresholdField);
    }
}