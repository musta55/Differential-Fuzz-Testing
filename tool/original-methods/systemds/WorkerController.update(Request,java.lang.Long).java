@Override
public FullHttpResponse update(Request request, Long objectId) {
    var result = workerService.get(objectId);
    if (result == null) {
        return Response.notFound(Constants.NOT_FOUND_MSG);
    }
    var model = MapperService.getModelFromBody(request, WorkerModel.class);
    model.id = objectId;
    // Setting address
    model.address = model.address == null ? result.address : model.address;
    // Setting name
    model.name = model.name == null ? result.name : model.name;
    workerService.update(model);
    model.setOnlineStatus(workerService.getWorkerOnlineStatus(model.id));
    return Response.ok(model.toString());
}