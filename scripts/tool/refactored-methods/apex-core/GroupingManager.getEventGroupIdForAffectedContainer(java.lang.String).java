/**
 * Returns deploy/undeploy group Id for container This could be a new
 * container allocated during redeploy process
 * @param containerId
 * @return groupId <br/>
 *         <b>Note:</b> groupId 0 indicates and indipendent event, with no
 *         group
 */
public EventGroupId getEventGroupIdForAffectedContainer(String containerId) {
    EventGroupId groupId = getEventGroupIdForContainer(containerId);
    if (groupId != null) {
        return groupId;
    }
    return findGroupIdByAffectedContainer(containerId);
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

