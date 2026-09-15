package org.apache.deltaspike.example.scheduler;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

public class SimpleSchedulerExample
{
    private static final Logger LOG = Logger.getLogger(SimpleSchedulerExample.class.getName());

    private SimpleSchedulerExample()
    {
    }

    public static void main(String[] args) throws InterruptedException
    {
        setup();
        try {
            GlobalResultHolder globalResultHolder =
                BeanProvider.getContextualReference(GlobalResultHolder.class);

            while (globalResultHolder.getCount() < 100)
            {
                Thread.sleep(500);
                LOG.info("current count: " + globalResultHolder.getCount());
            }
            LOG.info("completed!");
        } finally {
            teardown();
        }
    }

    private static void setup() {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        ContextControl contextControl = cdiContainer.getContextControl();
        contextControl.startContext(ApplicationScoped.class);
    }

    private static void teardown() {
        ContextControl contextControl = CdiContainerLoader.getCdiContainer().getContextControl();
        contextControl.stopContext(ApplicationScoped.class);
        CdiContainerLoader.getCdiContainer().shutdown();
    }
}