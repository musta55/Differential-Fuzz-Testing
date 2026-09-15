/**
 * Returns grouping groupId for operator which is to undergo deploy. Operators
 * undergoing deploy for first time will have groupId as 0
 * @param operatorId
 * @return groupId <br/>
 *         <b>Note:</b> groupId 0 indicates and indipendent event, with no
 *         group
 */
public EventGroupId getEventGroupIdForOperatorToDeploy(int operatorId) {
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getOperatorsToDeploy().contains(operatorId)) {
            return request.getEventGroupId();
        }
    }
    return null;
}