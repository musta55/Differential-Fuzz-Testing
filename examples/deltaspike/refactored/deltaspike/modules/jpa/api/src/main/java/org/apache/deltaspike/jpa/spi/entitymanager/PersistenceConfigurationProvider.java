package org.apache.deltaspike.jpa.spi.entitymanager;

import java.util.Properties;

/**
 * Provide the configuration for the EntityManagerFactory
 * which gets produced with a given {@link org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName}.
 *
 * By default we provide a configuration which can be configured
 * differently depending on the <i>-DdatabaseVendor</i> and the
 * {@link org.apache.deltaspike.core.api.projectstage.ProjectStage}
 */
public interface PersistenceConfigurationProvider
{

    /**
     * @param persistenceUnitName the name of the persistence unit in persistence.xml
     * @return the additional Properties from the configuration.
     */
    Properties getEntityManagerFactoryConfiguration(String persistenceUnitName);
}