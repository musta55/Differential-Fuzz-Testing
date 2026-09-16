public void addResourceLoaders(final BeforeBeanDiscovery beforeBeanDiscovery, final BeanManager beanManager) {
    beforeBeanDiscovery.addAnnotatedType(this.createAnnotatedType(ClasspathResourceProvider.class, beanManager));
    beforeBeanDiscovery.addAnnotatedType(this.createAnnotatedType(InjectableResourceProducer.class, beanManager));
    beforeBeanDiscovery.addAnnotatedType(this.createAnnotatedType(FileResourceProvider.class, beanManager));
}