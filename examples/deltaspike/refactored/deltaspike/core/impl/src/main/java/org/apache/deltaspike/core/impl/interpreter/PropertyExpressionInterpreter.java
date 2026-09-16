package org.apache.deltaspike.core.impl.interpreter;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.interpreter.BasePropertyExpressionInterpreter;

/**
 * Interpreter which uses the lookup chain of DeltaSpike for configured values
 */
public class PropertyExpressionInterpreter extends BasePropertyExpressionInterpreter
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getConfiguredValue(String key)
    {
        return ConfigResolver.getProjectStageAwarePropertyValue(key);
    }
}