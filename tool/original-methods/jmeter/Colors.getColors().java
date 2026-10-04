/**
 * Parse icon set file.
 * @return List of icons/action definition
 */
public static List<Color> getColors() {
    Properties defaultProps = JMeterUtils.loadProperties(DEFAULT_COLORS_PROPERTY_FILE);
    if (defaultProps == null) {
        JOptionPane.showMessageDialog(null, // $NON-NLS-1$
        JMeterUtils.getResString("toolbar_icon_set_not_found"), // $NON-NLS-1$
        JMeterUtils.getResString("toolbar_icon_set_not_found"), JOptionPane.WARNING_MESSAGE);
        return null;
    }
    Properties p;
    String userProp = JMeterUtils.getProperty(USER_DEFINED_COLORS_PROPERTY_FILE);
    if (userProp != null) {
        p = JMeterUtils.loadProperties(userProp, defaultProps);
    } else {
        p = defaultProps;
    }
    String order = JMeterUtils.getPropDefault(COLORS_ORDER, p.getProperty(ORDER_PROP_NAME));
    if (order == null) {
        log.warn("Could not find order list");
        JOptionPane.showMessageDialog(null, // $NON-NLS-1$
        JMeterUtils.getResString("toolbar_icon_set_not_found"), // $NON-NLS-1$
        JMeterUtils.getResString("toolbar_icon_set_not_found"), JOptionPane.WARNING_MESSAGE);
        return null;
    }
    String[] oList = order.split(ENTRY_SEP);
    List<Color> listColors = new ArrayList<>();
    for (String key : oList) {
        String trimmed = key.trim();
        String property = p.getProperty(trimmed);
        try {
            String[] lcol = property.split(ENTRY_SEP);
            Color itb = new Color(Integer.parseInt(lcol[0]), Integer.parseInt(lcol[1]), Integer.parseInt(lcol[2]));
            listColors.add(itb);
        } catch (java.lang.Exception e) {
            // $NON-NLS-1$
            log.warn("Error in colors.properties, current property={}", property);
        }
    }
    return listColors;
}