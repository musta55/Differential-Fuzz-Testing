package org.apache.deltaspike.data.impl.util.bean;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class BeanDestroyable<T> implements Destroyable {

    private final Bean<T> bean;
    private final T instance;
    private final CreationalContext<T> creationalContext;

    public BeanDestroyable(Bean<T> bean, T instance, CreationalContext<T> creationalContext) {
        this.bean = bean;
        this.instance = instance;
        this.creationalContext = creationalContext;
    }

    @Override
    public void destroy() {
        bean.destroy(instance, creationalContext);
    }
}