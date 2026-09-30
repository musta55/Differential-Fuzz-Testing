/**
 * Removes operator from grouping request
 */
public boolean removeOperatorFromGroupingRequest(int operatorId) {
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getOperatorsToDeploy().contains((operatorId))) {
            return request.removeOperatorToDeploy(operatorId);
        }
    }
    return false;
}