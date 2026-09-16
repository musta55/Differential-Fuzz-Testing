@Override
public Map<String, String> getAlternativeMapping() {
    Map<String, String> result = new HashMap<String, String>();
    String alternativeLabel = ConfigResolver.getPropertyValue(ACTIVE_ALTERNATIVE_LABEL_KEY);
    String activeQualifierLabel = null;
    if (alternativeLabel != null) {
        activeQualifierLabel = LABELED_ALTERNATIVES + "[" + alternativeLabel + "].";
    }
    Map<String, String> allProperties = ConfigResolver.getAllProperties();
    for (Map.Entry<String, String> property : allProperties.entrySet()) {
        if (activeQualifierLabel != null && property.getKey().startsWith(activeQualifierLabel)) {
            String interfaceName = property.getKey().substring(activeQualifierLabel.length());
            String implementation = property.getValue();
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Enabling labeled alternative for interface " + interfaceName + ": " + implementation);
            }
            result.put(interfaceName, implementation);
        } else if (property.getKey().startsWith(GLOBAL_ALTERNATIVES)) {
            String interfaceName = property.getKey().substring(GLOBAL_ALTERNATIVES.length());
            String implementation = property.getValue();
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Enabling global alternative for interface " + interfaceName + ": " + implementation);
            }
            if (//don't override labeled alternatives
            !result.containsKey(interfaceName)) {
                result.put(interfaceName, implementation);
            }
        }
    }
    return result;
}