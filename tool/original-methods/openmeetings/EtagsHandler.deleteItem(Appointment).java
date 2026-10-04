/**
 * {@inheritDoc}
 */
@Override
public boolean deleteItem(Appointment appointment) {
    if (calendar != null && calendar.getSyncType() != SyncType.NONE) {
        HttpDeleteMethod deleteMethod = null;
        try {
            String fullPath;
            if (Strings.isEmpty(appointment.getHref())) {
                // Make sure to set HREF just in case, if calendar exists but no href does.
                fullPath = this.path + appointment.getIcalId() + ".ics";
            } else {
                fullPath = getFullPath(URI.create(this.path), appointment.getHref());
            }
            deleteMethod = new HttpDeleteMethod(fullPath, appointment.getEtag());
            log.info("Deleting at location: {} with ETag: {}", fullPath, appointment.getEtag());
            HttpResponse response = client.execute(deleteMethod, context);
            int status = response.getStatusLine().getStatusCode();
            if (status == SC_NO_CONTENT || status == SC_OK || status == SC_NOT_FOUND) {
                log.info("Successfully deleted appointment with id: {}", appointment.getId());
                return true;
            } else {
                // Appointment Not deleted
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