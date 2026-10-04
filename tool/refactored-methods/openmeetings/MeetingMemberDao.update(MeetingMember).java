/**
 * Updating MeetingMember
 *
 * @param meetingMember - entity to update
 * @return - updated entity
 */
// -------------------------------------------------------------------------------
public MeetingMember update(MeetingMember meetingMember) {
    if (meetingMember.getId() == null) {
        persistMeetingMember(meetingMember);
    } else {
        mergeMeetingMember(meetingMember);
    }
    return meetingMember;
}
// ---- helper method(s) introduced by the refactoring ----
private void persistMeetingMember(MeetingMember meetingMember) {
    em.persist(meetingMember);
}

private void mergeMeetingMember(MeetingMember meetingMember) {
    if (!em.contains(meetingMember)) {
        meetingMember = em.merge(meetingMember);
    }
}

