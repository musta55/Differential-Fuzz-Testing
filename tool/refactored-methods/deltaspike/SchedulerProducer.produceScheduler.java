@Produces
@ApplicationScoped
protected Scheduler produceScheduler() {
    return schedulerExtension.getScheduler();
}