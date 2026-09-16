public void addResourceLoaders(final BeforeBeanDiscovery beforeBeanDiscovery, final BeanManager beanManager) {
    beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(ClasspathResourceProvider.class, beanManager));
    beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(InjectableResourceProducer.class, beanManager));
    beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(FileResourceProvider.class, beanManager));
}