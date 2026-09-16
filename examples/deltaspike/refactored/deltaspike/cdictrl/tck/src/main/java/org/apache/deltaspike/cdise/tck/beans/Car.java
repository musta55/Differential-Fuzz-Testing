package org.apache.deltaspike.cdise.tck.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class Car
{
    private static final ThreadLocal<Boolean> preDestroyCalled = ThreadLocal.withInitial(() -> false);

    @Inject
    private TestUser user;

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

    public TestUser getUser()
    {
        return user;
    }

    public static boolean isPreDestroyCalled()
    {
        return preDestroyCalled.get();
    }
}