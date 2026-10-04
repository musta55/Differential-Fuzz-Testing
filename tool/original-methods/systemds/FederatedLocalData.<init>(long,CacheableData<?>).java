public FederatedLocalData(long id, CacheableData<?> data) {
    super(data.getDataType(), null, data.getFileName());
    _fwh = new FederatedWorkerHandler(_flt, _frc, _fan);
    _data = data;
    long pid = Long.valueOf(IDHandler.getProcessID());
    ExecutionContextMap ecm = _flt.getECM(FederatedLookupTable.NOHOST, pid);
    synchronized (ecm) {
        ecm.get(-1).setVariable(Long.toString(id), _data);
    }
    setVarID(id);
}