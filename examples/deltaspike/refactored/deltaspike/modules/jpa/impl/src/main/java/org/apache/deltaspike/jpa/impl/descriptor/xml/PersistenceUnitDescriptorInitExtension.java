package org.apache.deltaspike.jpa.impl.descriptor.xml;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;
import org.apache.deltaspike.jpa.spi.descriptor.xml.PersistenceUnitDescriptorProvider;

public class PersistenceUnitDescriptorInitExtension implements Extension, Deactivatable
{
    private Boolean isActivated = true;
    
    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before)
    {
        isActivated = checkActivation();

        if (!isActivated)
        {
            return;
        }

        PersistenceUnitDescriptorProvider.getInstance().init();
    }

    private boolean checkActivation()
    {
        return ClassDeactivationUtils.isActivated(getClass());
    }
}