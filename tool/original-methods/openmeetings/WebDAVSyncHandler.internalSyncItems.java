/**
 * Sync using WebDAV-Sync.
 * @throws IOException on error
 * @throws DavException on error
 */
@Override
BaseDavRequest internalSyncItems() throws IOException, DavException {
    Long ownerId = this.calendar.getOwner().getId();
    boolean additionalSyncNeeded = false;
    DavPropertyNameSet properties = new DavPropertyNameSet();
    properties.add(DavPropertyName.GETETAG);
    // To return Calendar Data.
    properties.add(CalDAVConstants.DNAME_CALENDAR_DATA);
    //Create report to get
    SyncReportInfo reportInfo = new SyncReportInfo(calendar.getToken(), properties, SyncReportInfo.SYNC_LEVEL_1);
    SyncMethod method = new SyncMethod(path, reportInfo);
    HttpResponse httpResponse = client.execute(method, context);
    if (method.succeeded(httpResponse)) {
        //Map of Href and the Appointments, belonging to it.
        Map<String, Appointment> map = listToMap(appointmentDao.getbyCalendar(calendar.getId()));
        for (MultiStatusResponse response : method.getResponseBodyAsMultiStatus(httpResponse).getResponses()) {
            int status = response.getStatus()[0].getStatusCode();
            if (status == SC_OK) {
                Appointment a = map.get(response.getHref());
                if (a != null) {
                    //Old Event to get
                    String origetag = a.getEtag(), currentetag = CalendarDataProperty.getEtagfromResponse(response);
                    //If event modified, only then get it.
                    if (!currentetag.equals(origetag)) {
                        Calendar calendar = CalendarDataProperty.getCalendarfromResponse(response);
                        a = utils.parseCalendartoAppointment(a, calendar, currentetag);
                        appointmentDao.update(a, ownerId);
                    }
                } else {
                    //New Event, to get
                    String etag = CalendarDataProperty.getEtagfromResponse(response);
                    Calendar ical = CalendarDataProperty.getCalendarfromResponse(response);
                    Appointment appointments = utils.parseCalendartoAppointment(ical, response.getHref(), etag, calendar);
                    appointmentDao.update(appointments, ownerId);
                }
            } else if (status == SC_NOT_FOUND) {
                //Delete the Appointments not found on the server.
                Appointment a = map.get(response.getHref());
                //Only if the event exists on the database, delete it.
                if (a != null) {
                    appointmentDao.delete(a, calendar.getOwner().getId());
                }
            } else if (status == SC_INSUFFICIENT_SPACE_ON_RESOURCE) {
                additionalSyncNeeded = true;
            }
        }
        //Set the new token
        calendar.setToken(method.getResponseSynctoken(httpResponse));
    } else if (httpResponse.getStatusLine().getStatusCode() == SC_FORBIDDEN || httpResponse.getStatusLine().getStatusCode() == SC_PRECONDITION_FAILED) {
        //Specific case where a server might sometimes forget the sync token
        //Thus requiring a full sync needed to be done.
        log.info("Sync Token not accepted by server. Doing a full sync again.");
        calendar.setToken(null);
        additionalSyncNeeded = true;
    } else {
        log.error("Error in Sync Method Response with status code {}", httpResponse.getStatusLine().getStatusCode());
    }
    if (additionalSyncNeeded) {
        releaseConnection(method);
        return internalSyncItems();
    }
    return method;
}