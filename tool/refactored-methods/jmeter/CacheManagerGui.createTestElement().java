@Override
public TestElement createTestElement() {
    CacheManager element = new CacheManager();
    modifyTestElement(element);
    return element;
}
// ---- helper method(s) introduced by the refactoring ----
private static JCheckBox createCheckBox(String resourceKey, boolean selected) {
    return new JCheckBox(JMeterUtils.getResString(resourceKey), selected);
}

