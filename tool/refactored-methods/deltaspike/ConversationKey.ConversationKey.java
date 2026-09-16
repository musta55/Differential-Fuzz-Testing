public ConversationKey(Class<?> groupKey, Annotation... qualifiers) {
    this.groupKey = groupKey;
    this.qualifiers = new HashSet<>();
    //TODO maybe we have to add a real qualifier instead
    for (Annotation qualifier : qualifiers) {
        Class<? extends Annotation> annotationType = qualifier.annotationType();
        if (Any.class.isAssignableFrom(annotationType) || Default.class.isAssignableFrom(annotationType) || Named.class.isAssignableFrom(annotationType) || ConversationGroup.class.isAssignableFrom(annotationType)) {
            //won't be used for this key!
            continue;
        }
        this.qualifiers.add(qualifier);
    }
}