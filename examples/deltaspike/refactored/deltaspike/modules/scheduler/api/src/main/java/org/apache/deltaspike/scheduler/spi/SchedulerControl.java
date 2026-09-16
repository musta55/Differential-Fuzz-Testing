package org.apache.deltaspike.scheduler.spi;

/**
 * This interface provides high-level controls for the scheduler.
 *
 * It allows to control the scheduler as a whole ({@link #isSchedulerEnabled()}()) and on a per-job basis
 * ({@link #vetoJobExecution(Class)}.
 *
 * The interface is meant to be implemented by a CDI bean.
 */
public interface SchedulerControl
{
    /**
     * Control whether or not the scheduler should be started.
     *
     * @return if {@code true} the scheduler will be started, else not.
     */
    default boolean isSchedulerEnabled()
    {
        return true;
    }

    /**
     * Invoked each time a job is triggered, this controls whether the given job shall be started or not.
     *
     * NOTE: This only applies if the scheduler is actually running (see {@link #isSchedulerEnabled()}).
     *
     *  @param jobClass the job which was triggered
     * @return if {@code false} the job will be executed, else not.
     */
    default boolean vetoJobExecution(Class<?> jobClass)
    {
        return false;
    }
}