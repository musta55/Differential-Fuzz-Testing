@Override
public void notify(Class aClass) {
    MultipleDisable annotation = (MultipleDisable) aClass.getAnnotation(MultipleDisable.class);
    Disable[] valueList = annotation.value();
    if (valueList != null) {
        processDisables(valueList);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processDisables(Disable[] valueList) {
    for (Disable disable : valueList) {
        add(disable.value());
    }
}

