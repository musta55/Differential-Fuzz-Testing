@Override
public TestElement createTestElement() {
    CacheManager element = new CacheManager();
    modifyTestElement(element);
    controlledByThreadGroup.setSelected(element.getControlledByThread());
    clearEachIteration.setEnabled(!element.getControlledByThread());
    return element;
}