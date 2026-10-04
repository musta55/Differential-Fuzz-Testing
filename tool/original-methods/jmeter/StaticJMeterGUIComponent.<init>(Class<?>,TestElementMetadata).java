public StaticJMeterGUIComponent(Class<?> c, TestElementMetadata metadata) {
    this.labelResource = metadata.labelResource();
    String resourceBundle = metadata.resourceBundle();
    if (!resourceBundle.isEmpty()) {
        this.resourceBundle = ResourceBundle.getBundle(c.getName() + "Resources");
    } else if (labelResource.equals("displayName")) {
        this.resourceBundle = ResourceBundle.getBundle(c.getName() + "Resources");
    } else {
        this.resourceBundle = null;
    }
    this.groups = getGroups(c, metadata);
}