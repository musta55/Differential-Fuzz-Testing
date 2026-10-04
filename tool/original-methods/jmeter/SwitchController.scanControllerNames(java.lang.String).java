/**
 * @param sel controller name
 * @return index of controller named sel if present, otherwise index of default if found, otherwise {@link Integer#MAX_VALUE}
 */
private int scanControllerNames(String sel) {
    int i = 0;
    int defaultPos = Integer.MAX_VALUE;
    for (TestElement el : getSubControllers()) {
        String name = el.getName();
        if (name.equals(sel)) {
            return i;
        }
        if (name.equalsIgnoreCase("default")) {
            //$NON-NLS-1$
            defaultPos = i;
        }
        i++;
    }
    return defaultPos;
}