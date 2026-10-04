@Override
public void notify(Class aClass) {
    MultipleDisable annotation = (MultipleDisable) aClass.getAnnotation(MultipleDisable.class);
    Disable[] valueList = annotation.value();
    if (valueList != null) {
        for (Disable disable : valueList) {
            add(disable.value());
        }
    }
}