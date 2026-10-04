/**
 * Calls the JDBC code to be executed.
 */
protected void process() {
    if (JOrphanUtils.isBlank(getDataSource())) {
        throw new IllegalArgumentException("Name for DataSoure must not be empty in " + getName());
    }
    try (Connection conn = DataSourceElement.getConnection(getDataSource())) {
        execute(conn);
    } catch (SQLException ex) {
        handleSQLException(ex);
    } catch (IOException ex) {
        handleIOException(ex);
    } catch (UnsupportedOperationException ex) {
        handleUnsupportedOperationException(ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleSQLException(SQLException ex) {
    log.warn("SQL Problem in {}: {}", getName(), ex.toString());
}

private void handleIOException(IOException ex) {
    log.warn("IO Problem in {}: {}", getName(), ex.toString());
}

private void handleUnsupportedOperationException(UnsupportedOperationException ex) {
    log.warn("Execution Problem in {}: {}", getName(), ex.toString());
}

