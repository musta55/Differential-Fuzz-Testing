package org.apache.deltaspike.cdise.tck.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CarRepair
{
    private static ThreadLocal<Boolean> preDestroyCalled = ThreadLocal.withInitial(() -> false);

    @Inject
    private Car car;

    @PostConstruct
    protected void onPostConstruct()
    {
        preDestroyCalled.set(false);
    }

    @PreDestroy
    protected void onPreDestroy()
    {
        preDestroyCalled.set(true);
    }

    public Car getCar()
    {
        return car;
    }

    public static boolean isPreDestroyCalled()
    {
        return preDestroyCalled.get();
    }
}