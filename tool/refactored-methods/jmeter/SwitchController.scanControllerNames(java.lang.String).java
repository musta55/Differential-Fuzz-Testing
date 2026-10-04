/**
 * @param sel controller name
 * @return index of controller named sel if present, otherwise index of default if found, otherwise {@link Integer#MAX_VALUE}
 */
private int scanControllerNames(String sel) {
    int defaultPos = findDefaultPosition();
    for (int i = 0; i < getSubControllers().size(); i++) {
        TestElement el = getSubControllers().get(i);
        if (el.getName().equals(sel)) {
            return i;
        }
    }
    return defaultPos;
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

