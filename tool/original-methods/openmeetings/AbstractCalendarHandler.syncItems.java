/**
 * {@inheritDoc}
 */
@Override
public OmCalendar syncItems() {
    BaseDavRequest method = null;
    try {
        method = internalSyncItems();
    } catch (IOException | DavException e) {
        log.error("Error during the execution of calendar-multiget Report.", e);
    } catch (Exception e) {
        log.error("Severe Error during the execution of calendar-multiget Report.", e);
    } finally {
        releaseConnection(method);
    }
    return calendar;
}