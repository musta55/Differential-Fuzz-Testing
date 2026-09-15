package org.apache.deltaspike.core.spi.alternative;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

import java.util.Map;

public interface AlternativeBeanClassProvider extends Deactivatable
{
    /**
     * @return mapping between the interface of the bean an the alternative-bean-class
     */
    //instead of Class/Class we are using String/String to avoid classloader issues e.g. with EARs
    Map<String, String> getAlternativeMapping();
}