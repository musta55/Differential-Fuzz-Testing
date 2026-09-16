/**
 * Parse icon set file.
 * @return List of icons/action definition
 */
public static List<Color> getColors() {
    Properties defaultProps = loadProperties(DEFAULT_COLORS_PROPERTY_FILE);
    if (defaultProps == null) {
        // $NON-NLS-1$
        showErrorMessage(JMeterUtils.getResString("toolbar_icon_set_not_found"));
        return null;
    }
    Properties properties = getUserDefinedProperties(defaultProps);
    String order = getOrder(properties);
    if (order == null) {
        log.warn("Could not find order list");
        // $NON-NLS-1$
        showErrorMessage(JMeterUtils.getResString("toolbar_icon_set_not_found"));
        return null;
    }
    return parseColors(order, properties);
}
// ---- helper method(s) introduced by the refactoring ----
private static Properties loadProperties(String filePath) {
    return JMeterUtils.loadProperties(filePath);
}

private static void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, message, JOptionPane.WARNING_MESSAGE);
}

private static Properties getUserDefinedProperties(Properties defaultProps) {
    String userProp = JMeterUtils.getProperty(USER_DEFINED_COLORS_PROPERTY_FILE);
    return userProp != null ? JMeterUtils.loadProperties(userProp, defaultProps) : defaultProps;
}

private static String getOrder(Properties properties) {
    return JMeterUtils.getPropDefault(COLORS_ORDER, properties.getProperty(ORDER_PROP_NAME));
}

private static List<Color> parseColors(String order, Properties properties) {
    String[] oList = order.split(ENTRY_SEP);
    List<Color> listColors = new ArrayList<>();
    for (String key : oList) {
        String trimmed = key.trim();
        String property = properties.getProperty(trimmed);
        try {
            String[] lcol = property.split(ENTRY_SEP);
            Color color = new Color(Integer.parseInt(lcol[0]), Integer.parseInt(lcol[1]), Integer.parseInt(lcol[2]));
            listColors.add(color);
        } catch (Exception e) {
            // $NON-NLS-1$
            log.warn("Error in colors.properties, current property={}", property);
        }
    }
    return listColors;
}

