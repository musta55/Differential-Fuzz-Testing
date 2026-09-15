package org.apache.deltaspike.scheduler.impl;

import org.apache.deltaspike.scheduler.spi.Scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class SchedulerProducer
{
    @Inject
    private SchedulerExtension schedulerExtension;

    @Produces
    @ApplicationScoped
    protected Scheduler produceScheduler()
    {
        return schedulerExtension.getScheduler();
    }
}