@Override
public ListObject pull(int workerID) {
    ListObject model;
    try {
        model = _modelMap.get(workerID).take();
    } catch (InterruptedException e) {
        throw new DMLRuntimeException(e);
    }
    return model;
}