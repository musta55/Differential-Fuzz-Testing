@PreDestroy
public void cleanup() {
    for (DependentBeanEntry beanEntry : dependentBeanEntries) {
        try {
            beanEntry.getBean().destroy(beanEntry.getInstance(), beanEntry.getCreationalContext());
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    dependentBeanEntries.clear();
}