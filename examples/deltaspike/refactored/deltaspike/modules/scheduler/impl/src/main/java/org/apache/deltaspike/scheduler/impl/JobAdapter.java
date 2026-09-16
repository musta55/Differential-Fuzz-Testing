package org.apache.deltaspike.scheduler.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.enterprise.inject.Typed;

//configured via SchedulerBaseConfig
@Typed()
public class JobAdapter extends AbstractJobAdapter<Job>
{
    @Override
    protected Class<Job> getJobBaseClass()
    {
        return Job.class;
    }

    @Override
    public void execute(Job job, JobExecutionContext context) throws JobExecutionException
    {
        job.execute(context);
    }
}