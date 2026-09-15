package org.apache.deltaspike.jsf.impl.injection;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class AbstractBeanStorage
{
    private static final Logger LOG = Logger.getLogger(AbstractBeanStorage.class.getName());

    private List<DependentBeanEntry> dependentBeanEntries = new ArrayList<>();

    public void add(DependentBeanEntry dependentBeanEntry)
    {
        dependentBeanEntries.add(dependentBeanEntry);
    }

    @PreDestroy
    public void cleanup()
    {
        for (DependentBeanEntry beanEntry : dependentBeanEntries)
        {
            try
            {
                beanEntry.getBean().destroy(beanEntry.getInstance(), beanEntry.getCreationalContext());
            }
            catch (RuntimeException e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        dependentBeanEntries.clear();
    }
}