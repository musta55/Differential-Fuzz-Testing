package org.apache.deltaspike.scheduler.impl;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ClassUtils;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

public class CdiAwareJobFactory implements JobFactory
{
    private final JobFactory defaultFactory;

    public CdiAwareJobFactory()
    {
        String defaultJobFactoryName = SchedulerBaseConfig.JobCustomization.DEFAULT_JOB_FACTORY_CLASS_NAME;

        defaultFactory = ClassUtils.tryToInstantiateClassForName(defaultJobFactoryName, JobFactory.class);
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException
    {
        Job result = null;
        try
        {
            Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
            result = BeanProvider.getContextualReference(jobClass);
            scheduler.getContext().put(jobClass.getName(), Boolean.TRUE);
        }
        catch (Exception e)
        {
            handleException(result, bundle, scheduler);
        }
        return result;
    }

    private void handleException(Job result, TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException
    {
        if (result == null)
        {
            result = defaultFactory.newJob(bundle, scheduler);
        }
    }
}