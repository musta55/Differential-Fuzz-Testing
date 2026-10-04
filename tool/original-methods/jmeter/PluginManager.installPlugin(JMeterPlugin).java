private static void installPlugin(JMeterPlugin plugin) {
    String[][] icons = plugin.getIconMappings();
    ClassLoader classloader = plugin.getClass().getClassLoader();
    for (String[] icon : icons) {
        URL resource = classloader.getResource(icon[1].trim());
        if (resource == null) {
            log.warn("Can't find icon for {} - {}", icon[0], icon[1]);
        } else {
            final ImageIcon regularIcon = new ImageIcon(resource);
            GUIFactory.registerIcon(icon[0], regularIcon);
            ImageIcon disabledIcon = null;
            if (icon.length > 2 && icon[2] != null) {
                URL resource2 = classloader.getResource(icon[2].trim());
                if (resource2 == null) {
                    log.info("Can't find disabled icon for {} - {}", icon[0], icon[2]);
                } else {
                    disabledIcon = new ImageIcon(resource2);
                }
            } else {
                // Second icon is not specified, create disabled one automatically
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(regularIcon.getImage()));
            }
            if (disabledIcon != null) {
                GUIFactory.registerDisabledIcon(icon[0], disabledIcon);
            }
        }
    }
}