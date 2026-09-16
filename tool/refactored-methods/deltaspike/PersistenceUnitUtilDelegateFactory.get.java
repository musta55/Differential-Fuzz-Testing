public static PersistenceUnitUtil get(EntityManager entityManager) {
    final EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
    final String vendorName = getVendorName(entityManagerFactory);
    if ("openjpa".equalsIgnoreCase(vendorName)) {
        return new OpenJpaPersistenceUnitUtilDelegate(entityManager);
    }
    return entityManagerFactory.getPersistenceUnitUtil();
}
// ---- helper method(s) introduced by the refactoring ----
private static String getVendorName(EntityManagerFactory entityManagerFactory) {
    return (String) entityManagerFactory.getProperties().get("VendorName");
}

