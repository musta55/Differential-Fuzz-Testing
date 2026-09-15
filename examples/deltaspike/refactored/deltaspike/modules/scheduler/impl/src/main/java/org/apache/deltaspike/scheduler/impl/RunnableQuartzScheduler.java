package org.apache.deltaspike.scheduler.impl;

import org.apache.deltaspike.core.util.ClassUtils;
import org.quartz.Job;

//vetoed class (see SchedulerExtension)
public class RunnableQuartzScheduler extends AbstractQuartzScheduler<Runnable>
{
    private Class<? extends Job> runnableAdapter;

    @Override
    public void start()
    {
        String configuredAdapterClassName = SchedulerBaseConfig.JobCustomization.RUNNABLE_ADAPTER_CLASS_NAME;
        this.runnableAdapter = ClassUtils.tryToLoadClassForName(configuredAdapterClassName, Job.class);

        super.start();
    }

    @Override
    protected String getJobName(Class<?> jobClass)
    {
        return jobClass.getName();
    }

    @Override
    protected Class<? extends Job> createFinalJobClass(Class<? extends Runnable> jobClass)
    {
        return runnableAdapter;
    }
}