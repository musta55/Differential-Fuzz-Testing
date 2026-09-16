package org.apache.deltaspike.example.config;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

public class ConfigExample
{
    private static final Logger LOG = Logger.getLogger(ConfigExample.class.getName());

    private ConfigExample()
    {
    }

    public static void main(String[] args)
    {
        CdiContainer cdiContainer = null;
        try {
            cdiContainer = setupCdiContainer();
            SettingsBean settingsBean = BeanProvider.getContextualReference(SettingsBean.class, false);

            LOG.info("configured int-value #1: " + settingsBean.getIntProperty1());
            LOG.info("configured long-value #2: " + settingsBean.getProperty2());
            LOG.info("configured inverse-value #2: " + settingsBean.getInverseProperty());
            LOG.info("configured location (custom config): " + settingsBean.getLocationId().name());
        } finally {
            if (cdiContainer != null) {
                shutdownCdiContainer(cdiContainer);
            }
        }
    }

    private static CdiContainer setupCdiContainer() {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        ContextControl contextControl = cdiContainer.getContextControl();
        contextControl.startContext(ApplicationScoped.class);

        return cdiContainer;
    }

    private static void shutdownCdiContainer(CdiContainer cdiContainer) {
        cdiContainer.shutdown();
    }
}