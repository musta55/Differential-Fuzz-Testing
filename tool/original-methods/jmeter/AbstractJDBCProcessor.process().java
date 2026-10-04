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
        log.warn("SQL Problem in {}: {}", getName(), ex.toString());
    } catch (IOException ex) {
        log.warn("IO Problem in {}: {}", getName(), ex.toString());
    } catch (UnsupportedOperationException ex) {
        log.warn("Execution Problem in {}: {}", getName(), ex.toString());
    }
}