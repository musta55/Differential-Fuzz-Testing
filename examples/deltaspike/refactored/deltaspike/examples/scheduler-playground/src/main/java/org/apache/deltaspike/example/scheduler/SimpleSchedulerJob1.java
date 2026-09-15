package org.apache.deltaspike.example.scheduler;

import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.util.logging.Logger;

@Scheduled(cronExpression = "0/2 * * * * ?")
public class SimpleSchedulerJob1 implements Job
{
    private static final Logger LOG = Logger.getLogger(SimpleSchedulerJob1.class.getName());

    @Inject
    private GlobalResultHolder globalResultHolder;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        LOG.info("#increase called by " + getClass().getName());
        globalResultHolder.increase();
    }
}