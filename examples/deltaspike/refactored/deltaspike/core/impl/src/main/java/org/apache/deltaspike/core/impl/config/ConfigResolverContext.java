package org.apache.deltaspike.core.impl.config;

class ConfigResolverContext
{
    static final ConfigResolverContext NONE = new ConfigResolverContext();

    static final ConfigResolverContext EVAL_VARIABLES = new ConfigResolverContext().setEvaluateVariables(true);

    static final ConfigResolverContext PROJECTSTAGE = new ConfigResolverContext().setProjectStageAware(true);

    static final ConfigResolverContext PROJECTSTAGE_EVAL_VARIABLES = new ConfigResolverContext()
                                                                            .setProjectStageAware(true)
                                                                            .setEvaluateVariables(true);

    private boolean projectStageAware;
    private boolean evaluateVariables;
    private boolean propertyAware;

    ConfigResolverContext()
    {
    }
    
    ConfigResolverContext setEvaluateVariables(final boolean evaluateVariables)
    {
        this.evaluateVariables = evaluateVariables;
        return this;
    }   
    
    boolean isEvaluateVariables()
    {
        return evaluateVariables;
    }

    ConfigResolverContext setProjectStageAware(final boolean projectStageAware)
    {
        this.projectStageAware = projectStageAware;
        return this;
    }
    
    boolean isProjectStageAware()
    {
        return projectStageAware;
    }

    ConfigResolverContext setPropertyAware(final boolean propertyAware)
    {
        this.propertyAware = propertyAware;
        return this;
    }
    
    boolean isPropertyAware()
    {
        return propertyAware;
    }
}