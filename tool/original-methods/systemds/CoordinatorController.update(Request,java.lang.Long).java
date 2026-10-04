@Override
public FullHttpResponse update(Request request, Long objectId) {
    var result = coordinatorService.get(objectId);
    if (result == null) {
        return Response.notFound(Constants.NOT_FOUND_MSG);
    }
    var model = MapperService.getModelFromBody(request, CoordinatorModel.class);
    model.id = objectId;
    // Setting host
    model.host = model.host == null ? result.host : model.host;
    // Setting processId
    model.processId = model.processId == null ? result.processId : model.processId;
    // Setting name
    model.name = model.name == null ? result.name : model.name;
    model.generateMonitoringKey();
    coordinatorService.update(model);
    return Response.ok(model.toString());
}