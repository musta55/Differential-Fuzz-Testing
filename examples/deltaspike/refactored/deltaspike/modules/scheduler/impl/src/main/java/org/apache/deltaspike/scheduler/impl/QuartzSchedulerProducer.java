package org.apache.deltaspike.scheduler.impl;

import org.apache.deltaspike.scheduler.spi.Scheduler;
import org.quartz.Job;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@Alternative
public class QuartzSchedulerProducer extends SchedulerProducer
{
    @Produces
    @ApplicationScoped
    protected Scheduler<Job> produceScheduler()
    {
        return super.produceScheduler();
    }
}