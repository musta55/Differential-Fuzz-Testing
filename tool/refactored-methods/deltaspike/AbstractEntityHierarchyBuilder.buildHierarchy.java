public static void buildHierarchy(List<EntityDescriptor> entities, List<MappedSuperclassDescriptor> superClasses) {
    for (EntityDescriptor descriptor : entities) {
        processEntityDescriptor(descriptor, entities, superClasses);
    }
}
// ---- helper method(s) introduced by the refactoring ----
protected static void processEntityDescriptor(AbstractEntityDescriptor descriptor, List<EntityDescriptor> entities, List<MappedSuperclassDescriptor> superClasses) {
    Class<?> superClass = descriptor.getEntityClass().getSuperclass();
    while (superClass != null) {
        AbstractEntityDescriptor superDescriptor = findSuperClassDescriptor(superClass, entities, superClasses);
        if (superDescriptor != null) {
            setParentIfNotSet(descriptor, superDescriptor, entities, superClasses);
            descriptor.setParent(superDescriptor);
            return;
        }
        superClass = superClass.getSuperclass();
    }
}

private static AbstractEntityDescriptor findSuperClassDescriptor(Class<?> superClass, List<EntityDescriptor> entities, List<MappedSuperclassDescriptor> superClasses) {
    AbstractEntityDescriptor descriptorFromSuperClasses = findInSuperClasses(superClass, superClasses);
    if (descriptorFromSuperClasses != null) {
        return descriptorFromSuperClasses;
    }
    return findInEntities(superClass, entities);
}

private static AbstractEntityDescriptor findInSuperClasses(Class<?> superClass, List<MappedSuperclassDescriptor> superClasses) {
    for (MappedSuperclassDescriptor descriptor : superClasses) {
        if (descriptor.getEntityClass().equals(superClass)) {
            return descriptor;
        }
    }
    return null;
}

private static AbstractEntityDescriptor findInEntities(Class<?> superClass, List<EntityDescriptor> entities) {
    for (EntityDescriptor descriptor : entities) {
        if (descriptor.getEntityClass().equals(superClass)) {
            return descriptor;
        }
    }
    return null;
}

private static void setParentIfNotSet(AbstractEntityDescriptor descriptor, AbstractEntityDescriptor superDescriptor, List<EntityDescriptor> entities, List<MappedSuperclassDescriptor> superClasses) {
    if (descriptor.getParent() == null) {
        processEntityDescriptor(superDescriptor, entities, superClasses);
    }
}

