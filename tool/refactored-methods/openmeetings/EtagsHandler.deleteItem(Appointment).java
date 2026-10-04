/**
 * {@inheritDoc}
 */
@Override
public boolean deleteItem(Appointment appointment) {
    if (calendar != null && calendar.getSyncType() != SyncType.NONE) {
        HttpDeleteMethod deleteMethod = createDeleteMethod(appointment);
        if (deleteMethod == null) {
            return false;
        }
        try {
            HttpResponse response = client.execute(deleteMethod, context);
            int status = response.getStatusLine().getStatusCode();
            if (isSuccessfulDelete(status)) {
                log.info("Successfully deleted appointment with id: {}", appointment.getId());
                return true;
            }
        } catch (IOException e) {
            log.error("Error executing OptionsMethod during testConnection.", e);
        } catch (Exception e) {
            log.error("Severe Error in executing OptionsMethod during testConnection.", e);
        } finally {
            releaseConnection(deleteMethod);
        }
    }
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleInitialSync(MultiStatusResponse[] multiStatusResponses, Long ownerId) {
    for (MultiStatusResponse response : multiStatusResponses) {
        if (response.getStatus()[0].getStatusCode() == SC_OK) {
            String etag = CalendarDataProperty.getEtagfromResponse(response);
            Calendar ical = CalendarDataProperty.getCalendarfromResponse(response);
            Appointment appointment = utils.parseCalendartoAppointment(ical, response.getHref(), etag, calendar);
            appointmentDao.update(appointment, ownerId);
        }
    }
}

private void handleSubsequentSync(Map<String, Appointment> map, MultiStatusResponse[] multiStatusResponses, Long ownerId) {
    for (MultiStatusResponse response : multiStatusResponses) {
        if (response.getStatus()[0].getStatusCode() == SC_OK) {
            Appointment appointment = map.get(response.getHref());
            if (appointment != null) {
                updateExistingAppointment(appointment, response, ownerId);
            } else {
                addNewAppointment(response, ownerId);
            }
            map.remove(response.getHref());
        }
    }
    deleteMissingAppointments(map, ownerId);
}

private void updateExistingAppointment(Appointment appointment, MultiStatusResponse response, Long ownerId) {
    String origetag = appointment.getEtag();
    String currentetag = CalendarDataProperty.getEtagfromResponse(response);
    if (!currentetag.equals(origetag)) {
        Calendar calendar = CalendarDataProperty.getCalendarfromResponse(response);
        appointment = utils.parseCalendartoAppointment(appointment, calendar, currentetag);
        appointmentDao.update(appointment, ownerId);
    }
}

private void addNewAppointment(MultiStatusResponse response, Long ownerId) {
    String etag = CalendarDataProperty.getEtagfromResponse(response);
    Calendar ical = CalendarDataProperty.getCalendarfromResponse(response);
    Appointment appointment = utils.parseCalendartoAppointment(ical, response.getHref(), etag, calendar);
    appointmentDao.update(appointment, ownerId);
}

private void deleteMissingAppointments(Map<String, Appointment> map, Long ownerId) {
    for (Map.Entry<String, Appointment> entry : map.entrySet()) {
        appointmentDao.delete(entry.getValue(), ownerId);
    }
}

private HttpPutMethod createPutMethod(Appointment appointment) {
    try {
        CalendarOutputter calendarOutputter = new CalendarOutputter();
        Calendar ical = utils.parseAppointmenttoCalendar(appointment);
        CalendarRequest cr = new CalendarRequest(ical);
        String temp = determineHref(appointment);
        setEtagConditions(cr, appointment);
        return new HttpPutMethod(temp, cr, calendarOutputter);
    } catch (Exception e) {
        log.error("Error creating PutMethod.", e);
        return null;
    }
}

private String determineHref(Appointment appointment) {
    if (Strings.isEmpty(appointment.getHref())) {
        return UrlUtils.removeDoubleSlashes(this.path + appointment.getIcalId() + ".ics");
    } else {
        return getFullPath(URI.create(this.path), appointment.getHref());
    }
}

private void setEtagConditions(CalendarRequest cr, Appointment appointment) {
    if (Strings.isEmpty(appointment.getHref())) {
        cr.setIfNoneMatch(true);
        cr.setAllEtags(true);
    } else {
        cr.setIfMatch(true);
        cr.addEtag(appointment.getEtag());
    }
}

private void handleSuccessfulUpdate(Appointment appointment, HttpPutMethod putMethod) {
    String href = putMethod.getURI().getPath();
    appointment.setHref(href);
    Header etagh = putMethod.getFirstHeader("ETag");
    if (etagh != null) {
        appointment.setEtag(etagh.getValue());
        appointmentDao.update(appointment, appointment.getOwner().getId());
    } else {
        List<String> hrefs = Collections.singletonList(appointment.getHref());
        MultigetHandler multigetHandler = new MultigetHandler(hrefs, true, path, calendar, client, context, appointmentDao, utils);
        multigetHandler.syncItems();
    }
}

private HttpDeleteMethod createDeleteMethod(Appointment appointment) {
    String fullPath = determineDeletePath(appointment);
    if (fullPath == null) {
        return null;
    }
    log.info("Deleting at location: {} with ETag: {}", fullPath, appointment.getEtag());
    return new HttpDeleteMethod(fullPath, appointment.getEtag());
}

private String determineDeletePath(Appointment appointment) {
    if (Strings.isEmpty(appointment.getHref())) {
        return this.path + appointment.getIcalId() + ".ics";
    } else {
        return getFullPath(URI.create(this.path), appointment.getHref());
    }
}

private boolean isSuccessfulDelete(int status) {
    return status == SC_NO_CONTENT || status == SC_OK || status == SC_NOT_FOUND;
}

