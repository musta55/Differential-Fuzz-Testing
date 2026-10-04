/**
 * @return the selection value as a int with the value set to zero if it is out of range.
 */
private int getSelectionAsInt() {
    getProperty(SWITCH_VALUE).recoverRunningVersion(null);
    String sel = getSelection();
    if (StringUtils.isEmpty(sel)) {
        return 0;
    }
    if (StringUtils.isNumeric(sel)) {
        return parseNumericSelection(sel);
    }
    return scanControllerNames(sel);
}
// ---- helper method(s) introduced by the refactoring ----
private int parseNumericSelection(String sel) {
    try {
        int ret = Integer.parseInt(sel);
        if (ret < 0 || ret >= getSubControllers().size()) {
            // Out of range, we return first one
            ret = 0;
        }
        return ret;
    } catch (NumberFormatException e) {
        // it will be handled by code below
    }
    return 0;
}

private int findDefaultPosition() {
    for (int i = 0; i < getSubControllers().size(); i++) {
        TestElement el = getSubControllers().get(i);
        if (el.getName().equalsIgnoreCase("default")) {
            //$NON-NLS-1$
            return i;
        }
    }
    return Integer.MAX_VALUE;
}

