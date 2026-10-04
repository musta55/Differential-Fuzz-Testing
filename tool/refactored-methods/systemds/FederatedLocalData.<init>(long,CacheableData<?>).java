public FederatedLocalData(long id, CacheableData<?> data) {
    super(data.getDataType(), null, data.getFileName());
    _fwh = createFederatedWorkerHandler();
    _data = data;
    long pid = Long.valueOf(IDHandler.getProcessID());
    ExecutionContextMap ecm = _flt.getECM(FederatedLookupTable.NOHOST, pid);
    updateExecutionContextMap(ecm, id);
    setVarID(id);
}
// ---- helper method(s) introduced by the refactoring ----
private FederatedWorkerHandler createFederatedWorkerHandler() {
    return new FederatedWorkerHandler(_flt, _frc, _fan);
}

private void updateExecutionContextMap(ExecutionContextMap ecm, long id) {
    synchronized (ecm) {
        ecm.get(-1).setVariable(Long.toString(id), _data);
    }
}

