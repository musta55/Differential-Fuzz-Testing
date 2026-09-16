package org.apache.deltaspike.example.config;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SettingsBean
{
    @Inject
    @ConfigProperty(name = "property1")
    private Integer intProperty1;

    @Inject
    @Location
    private LocationId locationId;

    private Long property2;

    private Long inverseProperty;

    @Inject
    public SettingsBean(@Property2 Long property2, @Property2WithInverseSupport(inverseConvert = true) Long inverseProperty)
    {
        this.property2 = property2;
        this.inverseProperty = inverseProperty;
    }

    public Integer getIntProperty1()
    {
        return intProperty1;
    }

    public Long getProperty2()
    {
        return property2;
    }

    public Long getInverseProperty()
    {
        return inverseProperty;
    }

    public LocationId getLocationId()
    {
        return locationId;
    }
}