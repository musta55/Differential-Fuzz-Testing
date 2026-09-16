public static void buildHierarchy(List<EntityDescriptor> entities, List<MappedSuperclassDescriptor> superClasses) {
    for (EntityDescriptor descriptor : entities) {
        buildHierarchy(descriptor, entities, superClasses);
    }
}