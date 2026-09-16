package org.apache.deltaspike.core.spi.config;

import java.util.Set;

public interface ConfigValidator
{
    /**
     * @return a set of violation-messages if an invalid state is found
     * those messages will be used to add one deployment-problem per message
     */
    Set<String> processValidation();
}