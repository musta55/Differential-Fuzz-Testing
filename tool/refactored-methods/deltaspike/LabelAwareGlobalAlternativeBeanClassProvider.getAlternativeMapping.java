@Override
public Map<String, String> getAlternativeMapping() {
    Map<String, String> result = new HashMap<>();
    String alternativeLabel = ConfigResolver.getPropertyValue(ACTIVE_ALTERNATIVE_LABEL_KEY);
    String activeQualifierLabel = getActiveQualifierLabel(alternativeLabel);
    Map<String, String> allProperties = ConfigResolver.getAllProperties();
    for (Map.Entry<String, String> property : allProperties.entrySet()) {
        String key = property.getKey();
        String value = property.getValue();
        if (activeQualifierLabel != null && key.startsWith(activeQualifierLabel)) {
            addLabeledAlternative(result, key.substring(activeQualifierLabel.length()), value);
        } else if (key.startsWith(GLOBAL_ALTERNATIVES)) {
            addGlobalAlternative(result, key.substring(GLOBAL_ALTERNATIVES.length()), value);
        }
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private String getActiveQualifierLabel(String alternativeLabel) {
    return alternativeLabel != null ? LABELED_ALTERNATIVES + "[" + alternativeLabel + "]." : null;
}

private void addLabeledAlternative(Map<String, String> result, String interfaceName, String implementation) {
    logAlternative("Enabling labeled alternative for interface", interfaceName, implementation);
    result.put(interfaceName, implementation);
}

private void addGlobalAlternative(Map<String, String> result, String interfaceName, String implementation) {
    if (!result.containsKey(interfaceName)) {
        logAlternative("Enabling global alternative for interface", interfaceName, implementation);
        result.put(interfaceName, implementation);
    }
}

private void logAlternative(String message, String interfaceName, String implementation) {
    if (LOG.isLoggable(Level.FINE)) {
        LOG.fine(message + " " + interfaceName + ": " + implementation);
    }
}

