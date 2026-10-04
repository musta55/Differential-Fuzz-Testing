@Override
public ListObject pull(int workerID) {
    return getModelForWorker(workerID);
}
// ---- helper method(s) introduced by the refactoring ----
private ListObject getModelForWorker(int workerID) {
    ListObject model;
    try {
        model = _modelMap.get(workerID).take();
    } catch (InterruptedException e) {
        throw new DMLRuntimeException(e);
    }
    return model;
}

