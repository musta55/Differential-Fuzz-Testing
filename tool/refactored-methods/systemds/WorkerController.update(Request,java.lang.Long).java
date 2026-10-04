@Override
public FullHttpResponse update(Request request, Long objectId) {
    var result = workerService.get(objectId);
    if (result == null) {
        return Response.notFound(Constants.NOT_FOUND_MSG);
    }
    var model = MapperService.getModelFromBody(request, WorkerModel.class);
    model.id = objectId;
    setFieldsIfNotNull(model, result);
    workerService.update(model);
    model.setOnlineStatus(workerService.getWorkerOnlineStatus(model.id));
    return Response.ok(model.toString());
}
// ---- helper method(s) introduced by the refactoring ----
private void setFieldsIfNotNull(WorkerModel target, WorkerModel source) {
    target.address = target.address == null ? source.address : target.address;
    target.name = target.name == null ? source.name : target.name;
}

