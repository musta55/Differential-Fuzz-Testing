package org.apache.deltaspike.scheduler.impl;

import org.quartz.Job;

//vetoed class (see SchedulerExtension)
public class JobQuartzScheduler extends AbstractQuartzScheduler<Job>
{
    @Override
    protected String getJobName(Class<?> jobClass)
    {
        return jobClass.getName();
    }

    @Override
    protected Class<? extends Job> createFinalJobClass(Class<? extends Job> jobClass)
    {
        return JobAdapter.class;
    }
}