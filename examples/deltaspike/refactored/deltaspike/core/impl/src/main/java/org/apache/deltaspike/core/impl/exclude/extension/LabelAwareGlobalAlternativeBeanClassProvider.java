package org.apache.deltaspike.core.impl.exclude.extension;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.spi.alternative.AlternativeBeanClassProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabelAwareGlobalAlternativeBeanClassProvider implements AlternativeBeanClassProvider
{
    private static final String GLOBAL_ALTERNATIVES = "globalAlternatives.";
    private static final String LABELED_ALTERNATIVES = "labeledAlternatives";
    private static final String ACTIVE_ALTERNATIVE_LABEL_KEY = "activeAlternativeLabel";

    private static final Logger LOG = Logger.getLogger(LabelAwareGlobalAlternativeBeanClassProvider.class.getName());

    @Override
    public Map<String, String> getAlternativeMapping()
    {
        Map<String, String> result = new HashMap<>();

        String alternativeLabel = ConfigResolver.getPropertyValue(ACTIVE_ALTERNATIVE_LABEL_KEY);
        String activeQualifierLabel = getActiveQualifierLabel(alternativeLabel);

        Map<String, String> allProperties = ConfigResolver.getAllProperties();
        for (Map.Entry<String, String> property : allProperties.entrySet())
        {
            String key = property.getKey();
            String value = property.getValue();

            if (activeQualifierLabel != null && key.startsWith(activeQualifierLabel))
            {
                addLabeledAlternative(result, key.substring(activeQualifierLabel.length()), value);
            }
            else if (key.startsWith(GLOBAL_ALTERNATIVES))
            {
                addGlobalAlternative(result, key.substring(GLOBAL_ALTERNATIVES.length()), value);
            }
        }

        return result;
    }

    private String getActiveQualifierLabel(String alternativeLabel)
    {
        return alternativeLabel != null ? LABELED_ALTERNATIVES + "[" + alternativeLabel + "]." : null;
    }

    private void addLabeledAlternative(Map<String, String> result, String interfaceName, String implementation)
    {
        logAlternative("Enabling labeled alternative for interface", interfaceName, implementation);
        result.put(interfaceName, implementation);
    }

    private void addGlobalAlternative(Map<String, String> result, String interfaceName, String implementation)
    {
        if (!result.containsKey(interfaceName))
        {
            logAlternative("Enabling global alternative for interface", interfaceName, implementation);
            result.put(interfaceName, implementation);
        }
    }

    private void logAlternative(String message, String interfaceName, String implementation)
    {
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine(message + " " + interfaceName + ": " + implementation);
        }
    }
}