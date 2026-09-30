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
    for (GroupingRequest request : getGroupingRequests().values()) {
        if (request.getAffectedContainers().contains(containerId)) {
            groupId = request.getEventGroupId();
        }
    }
    return groupId;
}