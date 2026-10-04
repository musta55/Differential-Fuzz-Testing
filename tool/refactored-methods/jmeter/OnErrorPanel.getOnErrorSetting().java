public int getOnErrorSetting() {
    for (Map.Entry<Integer, JRadioButton> entry : buttonMap.entrySet()) {
        if (entry.getValue().isSelected()) {
            return entry.getKey();
        }
    }
    // Defaults to continue
    return OnErrorTestElement.ON_ERROR_CONTINUE;
}