protected AbstractCalendarHandler(String path, OmCalendar calendar, HttpClient client, HttpClientContext context, AppointmentDao appointmentDao, IcalUtils utils) {
    initFields(path, calendar, client, context, appointmentDao, utils);
}
// ---- helper method(s) introduced by the refactoring ----
private void initFields(String path, OmCalendar calendar, HttpClient client, HttpClientContext context, AppointmentDao appointmentDao, IcalUtils utils) {
    this.path = path;
    this.calendar = calendar;
    this.client = client;
    this.context = context;
    this.appointmentDao = appointmentDao;
    this.utils = utils;
}

private void handleIOException(IOException e) {
    log.error("Error during the execution of calendar-multiget Report.", e);
}

private void handleDavException(DavException e) {
    log.error("Error during the execution of calendar-multiget Report.", e);
}

private void handleGeneralException(Exception e) {
    log.error("Severe Error during the execution of calendar-multiget Report.", e);
}

