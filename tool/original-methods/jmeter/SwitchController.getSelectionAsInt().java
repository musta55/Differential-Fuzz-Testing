/**
 * @return the selection value as a int with the value set to zero if it is out of range.
 */
private int getSelectionAsInt() {
    getProperty(SWITCH_VALUE).recoverRunningVersion(null);
    String sel = getSelection();
    if (StringUtils.isEmpty(sel)) {
        return 0;
    } else {
        try {
            if (StringUtils.isNumeric(sel)) {
                int ret = Integer.parseInt(sel);
                if (ret < 0 || ret >= getSubControllers().size()) {
                    // Out of range, we return first one
                    ret = 0;
                }
                return ret;
            }
        } catch (NumberFormatException e) {
            // it will be handled by code below
        }
        return scanControllerNames(sel);
    }
}