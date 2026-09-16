/**
 * {@inheritDoc}
 */
@Override
BaseDavRequest internalSyncItems() throws IOException, DavException {
    Long ownerId = this.calendar.getOwner().getId();
    Map<String, Appointment> map = listToMap(appointmentDao.getbyCalendar(calendar.getId()));
    DavPropertyNameSet properties = new DavPropertyNameSet();
    properties.add(DavPropertyName.GETETAG);
    CompFilter vcalendar = new CompFilter(Calendar.VCALENDAR);
    vcalendar.addCompFilter(new CompFilter(Component.VEVENT));
    CalendarQuery query = new CalendarQuery(properties, vcalendar, new CalendarData(), false, false);
    HttpCalDAVReportMethod method = new HttpCalDAVReportMethod(path, query, CalDAVConstants.DEPTH_1);
    HttpResponse httpResponse = client.execute(method, context);
    if (method.succeeded(httpResponse)) {
        MultiStatusResponse[] multiStatusResponses = method.getResponseBodyAsMultiStatus(httpResponse).getResponses();
        if (map.isEmpty()) {
            //Initializing the Calendar for the first time.
            //Parse the responses into Appointments
            for (MultiStatusResponse response : multiStatusResponses) {
                if (response.getStatus()[0].getStatusCode() == SC_OK) {
                    String etag = CalendarDataProperty.getEtagfromResponse(response);
                    Calendar ical = CalendarDataProperty.getCalendarfromResponse(response);
                    Appointment appointments = utils.parseCalendartoAppointment(ical, response.getHref(), etag, calendar);
                    appointmentDao.update(appointments, ownerId);
                }
            }
        } else {
            //Calendar has been inited before
            for (MultiStatusResponse response : multiStatusResponses) {
                if (response.getStatus()[0].getStatusCode() == SC_OK) {
                    Appointment appointment = map.get(response.getHref());
                    //Event updated
                    if (appointment != null) {
                        String origetag = appointment.getEtag(), currentetag = CalendarDataProperty.getEtagfromResponse(response);
                        //If etag is modified
                        if (!currentetag.equals(origetag)) {
                            Calendar calendar = CalendarDataProperty.getCalendarfromResponse(response);
                            appointment = utils.parseCalendartoAppointment(appointment, calendar, currentetag);
                            appointmentDao.update(appointment, ownerId);
                        }
                        map.remove(response.getHref());
                    } else {
                        // The orig list of events doesn't contain this event.
                        String etag = CalendarDataProperty.getEtagfromResponse(response);
                        Calendar ical = CalendarDataProperty.getCalendarfromResponse(response);
                        Appointment appointments = utils.parseCalendartoAppointment(ical, response.getHref(), etag, calendar);
                        appointmentDao.update(appointments, ownerId);
                    }
                }
            }
            //Remaining Events have been deleted on the server, thus delete them
            for (Map.Entry<String, Appointment> entry : map.entrySet()) {
                appointmentDao.delete(entry.getValue(), ownerId);
            }
        }
    } else {
        log.error("Report Method return Status: {} for calId {} ", httpResponse.getStatusLine().getStatusCode(), calendar.getId());
    }
    return method;
}