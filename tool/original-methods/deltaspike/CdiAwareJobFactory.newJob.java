@Override
public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    Job result = null;
    try {
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        result = BeanProvider.getContextualReference(jobClass);
        scheduler.getContext().put(jobClass.getName(), Boolean.TRUE);
    } catch (Exception e) {
        if (result == null) {
            result = defaultFactory.newJob(bundle, scheduler);
        }
    }
    return result;
}