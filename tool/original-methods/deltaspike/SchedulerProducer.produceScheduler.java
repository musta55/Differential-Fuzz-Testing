@Produces
@ApplicationScoped
protected Scheduler produceScheduler() {
    return this.schedulerExtension.getScheduler();
}