/**
 * Returns grouping groupId for operator which is to undergo deploy. Operators
 * undergoing deploy for first time will have groupId as 0
 * @param operatorId
 * @return groupId <br/>
 *         <b>Note:</b> groupId 0 indicates and indipendent event, with no
 *         group
 */
public EventGroupId getEventGroupIdForOperatorToDeploy(int operatorId) {
    return findGroupIdByOperatorToDeploy(operatorId);
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

