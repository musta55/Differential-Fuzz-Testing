package org.apache.deltaspike.core.impl.resourceloader;

import org.apache.deltaspike.core.api.resourceloader.ClasspathResourceProvider;
import org.apache.deltaspike.core.api.resourceloader.FileResourceProvider;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * This is needed for certain class loading cases (EARs, external modules).
 * Simply registers additional resource loader classes to the context.
 */
//TODO re-visit it based on DELTASPIKE-472
public class ResourceLoaderExtension implements Extension
{
    public void addResourceLoaders(final BeforeBeanDiscovery beforeBeanDiscovery, final BeanManager beanManager)
    {
        beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(ClasspathResourceProvider.class, beanManager));
        beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(InjectableResourceProducer.class, beanManager));
        beforeBeanDiscovery.addAnnotatedType(createAnnotatedType(FileResourceProvider.class, beanManager));
    }

    private AnnotatedType<?> createAnnotatedType(final Class<?> clazz, final BeanManager beanManager)
    {
        return beanManager.createAnnotatedType(clazz);
    }
}