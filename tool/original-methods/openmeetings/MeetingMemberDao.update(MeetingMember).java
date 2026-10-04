/**
 * Updating MeetingMember
 *
 * @param meetingMember - entity to update
 * @return - updated entity
 */
// -------------------------------------------------------------------------------
public MeetingMember update(MeetingMember meetingMember) {
    if (meetingMember.getId() == null) {
        em.persist(meetingMember);
    } else {
        if (!em.contains(meetingMember)) {
            meetingMember = em.merge(meetingMember);
        }
    }
    return meetingMember;
}