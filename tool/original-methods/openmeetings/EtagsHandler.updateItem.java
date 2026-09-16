/**
 * {@inheritDoc}
 */
@Override
public boolean updateItem(Appointment appointment) {
    OmCalendar calendar = appointment.getCalendar();
    String href;
    if (calendar != null && calendar.getSyncType() != SyncType.NONE) {
        //Store new Appointment on the server
        HttpPutMethod putMethod = null;
        try {
            List<String> hrefs = null;
            CalendarOutputter calendarOutputter = new CalendarOutputter();
            String temp = null;
            Calendar ical = utils.parseAppointmenttoCalendar(appointment);
            CalendarRequest cr = new CalendarRequest(ical);
            if (Strings.isEmpty(appointment.getHref())) {
                temp = this.path + appointment.getIcalId() + ".ics";
                temp = UrlUtils.removeDoubleSlashes(temp);
                cr.setIfNoneMatch(true);
                cr.setAllEtags(true);
            } else {
                temp = getFullPath(URI.create(this.path), appointment.getHref());
                cr.setIfMatch(true);
                cr.addEtag(appointment.getEtag());
            }
            putMethod = new HttpPutMethod(temp, cr, calendarOutputter);
            HttpResponse httpResponse = client.execute(putMethod, context);
            if (putMethod.succeeded(httpResponse)) {
                // Set the href as the path
                href = putMethod.getURI().getPath();
                appointment.setHref(href);
                //Check if the ETag header was returned.
                Header etagh = putMethod.getFirstHeader("ETag");
                if (etagh == null) {
                    hrefs = Collections.singletonList(appointment.getHref());
                } else {
                    appointment.setEtag(etagh.getValue());
                    appointmentDao.update(appointment, appointment.getOwner().getId());
                }
            } else {
                //Appointment not created on the server
                return false;
            }
            //Get new etags for the ones which didn't return an ETag header
            MultigetHandler multigetHandler = new MultigetHandler(hrefs, true, path, calendar, client, context, appointmentDao, utils);
            multigetHandler.syncItems();
            return true;
        } catch (IOException e) {
            log.error("Error executing OptionsMethod during testConnection.", e);
        } catch (Exception e) {
            log.error("Severe Error in executing OptionsMethod during testConnection.", e);
        } finally {
            releaseConnection(putMethod);
        }
    }
    return false;
}