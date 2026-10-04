/**
 * Sync using WebDAV-Sync.
 * @throws IOException on error
 * @throws DavException on error
 */
@Override
BaseDavRequest internalSyncItems() throws IOException, DavException {
    Long ownerId = this.calendar.getOwner().getId();
    boolean additionalSyncNeeded = false;
    DavPropertyNameSet properties = createPropertiesSet();
    SyncReportInfo reportInfo = new SyncReportInfo(calendar.getToken(), properties, SyncReportInfo.SYNC_LEVEL_1);
    SyncMethod method = new SyncMethod(path, reportInfo);
    HttpResponse httpResponse = client.execute(method, context);
    if (method.succeeded(httpResponse)) {
        Map<String, Appointment> map = listToMap(appointmentDao.getbyCalendar(calendar.getId()));
        processResponses(map, method, httpResponse, ownerId);
        calendar.setToken(method.getResponseSynctoken(httpResponse));
    } else {
        handleErrorResponse(httpResponse, method);
        additionalSyncNeeded = true;
    }
    if (additionalSyncNeeded) {
        releaseConnection(method);
        return internalSyncItems();
    }
    return method;
}
// ---- helper method(s) introduced by the refactoring ----
private DavPropertyNameSet createPropertiesSet() {
    DavPropertyNameSet properties = new DavPropertyNameSet();
    properties.add(DavPropertyName.GETETAG);
    properties.add(CalDAVConstants.DNAME_CALENDAR_DATA);
    return properties;
}

private void processResponses(Map<String, Appointment> map, SyncMethod method, HttpResponse httpResponse, Long ownerId) throws DavException {
    for (MultiStatusResponse response : method.getResponseBodyAsMultiStatus(httpResponse).getResponses()) {
        int status = response.getStatus()[0].getStatusCode();
        switch(status) {
            case SC_OK:
                processOkResponse(map, response, ownerId);
                break;
            case SC_NOT_FOUND:
                processNotFoundResponse(map, response);
                break;
            case SC_INSUFFICIENT_SPACE_ON_RESOURCE:
                log.warn("Insufficient space on resource during sync.");
                break;
            default:
                log.error("Unexpected status code during sync: {}", status);
        }
    }
}

private void processOkResponse(Map<String, Appointment> map, MultiStatusResponse response, Long ownerId) throws DavException {
    String href = response.getHref();
    Appointment appointment = map.get(href);
    String currentEtag = CalendarDataProperty.getEtagfromResponse(response);
    Calendar calendarData = CalendarDataProperty.getCalendarfromResponse(response);
    if (appointment != null && !appointment.getEtag().equals(currentEtag)) {
        appointment = utils.parseCalendartoAppointment(appointment, calendarData, currentEtag);
        appointmentDao.update(appointment, ownerId);
    } else if (appointment == null) {
        Appointment newAppointment = utils.parseCalendartoAppointment(calendarData, href, currentEtag, calendar);
        appointmentDao.update(newAppointment, ownerId);
    }
}

private void processNotFoundResponse(Map<String, Appointment> map, MultiStatusResponse response) {
    Appointment appointment = map.get(response.getHref());
    if (appointment != null) {
        appointmentDao.delete(appointment, calendar.getOwner().getId());
    }
}

private void handleErrorResponse(HttpResponse httpResponse, SyncMethod method) {
    int statusCode = httpResponse.getStatusLine().getStatusCode();
    if (statusCode == SC_FORBIDDEN || statusCode == SC_PRECONDITION_FAILED) {
        log.info("Sync Token not accepted by server. Doing a full sync again.");
        calendar.setToken(null);
    } else {
        log.error("Error in Sync Method Response with status code {}", statusCode);
    }
    releaseConnection(method);
}

