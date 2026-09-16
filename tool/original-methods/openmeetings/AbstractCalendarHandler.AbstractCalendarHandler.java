protected AbstractCalendarHandler(String path, OmCalendar calendar, HttpClient client, HttpClientContext context, AppointmentDao appointmentDao, IcalUtils utils) {
    this.path = path;
    this.calendar = calendar;
    this.client = client;
    this.context = context;
    this.appointmentDao = appointmentDao;
    this.utils = utils;
}