public static PersistenceUnitUtil get(EntityManager entityManager) {
    final EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
    final String vendorName = (String) entityManagerFactory.getProperties().get("VendorName");
    if (vendorName != null && "openjpa".equalsIgnoreCase(vendorName)) {
        return new OpenJpaPersistenceUnitUtilDelegate(entityManager);
    }
    return entityManagerFactory.getPersistenceUnitUtil();
}