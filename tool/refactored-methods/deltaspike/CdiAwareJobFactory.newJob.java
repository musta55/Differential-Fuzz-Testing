@Override
public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    Job result = null;
    try {
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        result = BeanProvider.getContextualReference(jobClass);
        scheduler.getContext().put(jobClass.getName(), Boolean.TRUE);
    } catch (Exception e) {
        handleException(result, bundle, scheduler);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleException(Job result, TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
    if (result == null) {
        result = defaultFactory.newJob(bundle, scheduler);
    }
}

