public ConversationKey(Class<?> groupKey, Annotation... qualifiers) {
    this.groupKey = groupKey;
    //TODO maybe we have to add a real qualifier instead
    Class<? extends Annotation> annotationType;
    for (Annotation qualifier : qualifiers) {
        annotationType = qualifier.annotationType();
        if (Any.class.isAssignableFrom(annotationType) || Default.class.isAssignableFrom(annotationType) || Named.class.isAssignableFrom(annotationType) || ConversationGroup.class.isAssignableFrom(annotationType)) {
            //won't be used for this key!
            continue;
        }
        if (this.qualifiers == null) {
            this.qualifiers = new HashSet<Annotation>();
        }
        this.qualifiers.add(qualifier);
    }
}