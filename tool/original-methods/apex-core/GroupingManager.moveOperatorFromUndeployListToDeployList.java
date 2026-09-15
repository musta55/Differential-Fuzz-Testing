/**
 * When operator state changes from PENDING_UNDEPLOY to PENDING_DEPLOY move
 * operator from operatorsToUndeploy to operatorsToDeploy
 * @param operator
 * @return groupId
 */
public EventGroupId moveOperatorFromUndeployListToDeployList(PTOperator oper) {
    EventGroupId groupId = null;
    for (GroupingRequest request : groupingRequests.values()) {
        if (request.getOperatorsToUndeploy().contains(oper.getId())) {
            groupId = request.getEventGroupId();
            request.removeOperatorToUndeploy(oper.getId());
            request.addOperatorToDeploy(oper.getId());
        }
    }
    return groupId;
}