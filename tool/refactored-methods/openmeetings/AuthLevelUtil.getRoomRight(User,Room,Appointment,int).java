public static Set<Room.Right> getRoomRight(User u, Room r, Appointment a, int userCount) {
    Set<Room.Right> result = new HashSet<>();
    if (u == null) {
        return result;
    }
    if (isSuperModerator(u, r, a)) {
        result.add(Room.Right.SUPER_MODERATOR);
    }
    if (result.isEmpty()) {
        addModeratorRights(u, r, userCount, result);
    }
    if (isConferenceRoomWithoutModerator(r, result)) {
        addDefaultConferenceRights(result);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isSuperModerator(User u, Room r, Appointment a) {
    return hasAdminLevel(u.getRights()) || Objects.equal(u.getId(), r.getOwnerId()) || (r.isAppointment() && a != null && u.getId().equals(a.getOwner().getId()));
}

private static void addModeratorRights(User u, Room r, int userCount, Set<Room.Right> result) {
    if (!r.isModerated() && userCount == 1) {
        result.add(Room.Right.MODERATOR);
    }
    for (RoomModerator rm : r.getModerators()) {
        if (u.getId().equals(rm.getUser().getId())) {
            result.add(rm.isSuperModerator() ? Room.Right.SUPER_MODERATOR : Room.Right.MODERATOR);
            break;
        }
    }
    if (result.isEmpty() && r.getGroups() != null && !r.getGroups().isEmpty()) {
        for (RoomGroup rg : r.getGroups()) {
            for (GroupUser gu : u.getGroupUsers()) {
                if (gu.getGroup().getId().equals(rg.getGroup().getId()) && gu.isModerator()) {
                    result.add(Room.Right.MODERATOR);
                    break;
                }
            }
            if (!result.isEmpty()) {
                break;
            }
        }
    }
}

private static boolean isConferenceRoomWithoutModerator(Room r, Set<Room.Right> result) {
    return Room.Type.CONFERENCE == r.getType() && !result.contains(Room.Right.SUPER_MODERATOR) && !result.contains(Room.Right.MODERATOR) && !result.contains(Room.Right.VIDEO);
}

private static void addDefaultConferenceRights(Set<Room.Right> result) {
    result.add(Room.Right.AUDIO);
    result.add(Room.Right.VIDEO);
}

