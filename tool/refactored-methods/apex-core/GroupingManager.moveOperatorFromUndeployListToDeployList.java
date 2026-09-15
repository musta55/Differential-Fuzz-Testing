/**
 * When operator state changes from PENDING_UNDEPLOY to PENDING_DEPLOY move
 * operator from operatorsToUndeploy to operatorsToDeploy
 * @param operator
 * @return groupId
 */
public EventGroupId moveOperatorFromUndeployListToDeployList(PTOperator oper) {
    return moveOperatorAndReturnGroupId(oper);
}
// ---- helper method(s) introduced by the refactoring ----
private EventGroupId findGroupIdByAffectedContainer(String containerId) {
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getAffectedContainers().contains(containerId)) {
            return request.getEventGroupId();
        }
    }
    return null;
}

private EventGroupId findGroupIdByOperatorToDeploy(int operatorId) {
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getOperatorsToDeploy().contains(operatorId)) {
            return request.getEventGroupId();
        }
    }
    return null;
}

private boolean removeOperatorFromRequests(int operatorId) {
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getOperatorsToDeploy().contains(operatorId)) {
            return request.removeOperatorToDeploy(operatorId);
        }
    }
    return false;
}

private EventGroupId moveOperatorAndReturnGroupId(PTOperator oper) {
    for (GroupingRequest request : groupingRequests.values()) {
        if (request.getOperatorsToUndeploy().contains(oper.getId())) {
            EventGroupId groupId = request.getEventGroupId();
            request.removeOperatorToUndeploy(oper.getId());
            request.addOperatorToDeploy(oper.getId());
            return groupId;
        }
    }
    return null;
}

