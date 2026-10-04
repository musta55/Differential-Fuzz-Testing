public static Set<Room.Right> getRoomRight(User u, Room r, Appointment a, int userCount) {
    Set<Room.Right> result = new HashSet<>();
    if (u == null) {
        return result;
    }
    if (//admin user get superModerator level, no-one can kick him/her
    hasAdminLevel(u.getRights()) || // user personal room
    Objects.equal(u.getId(), r.getOwnerId()) || // appointment owner is super moderator
    (r.isAppointment() && a != null && u.getId().equals(a.getOwner().getId()))) {
        result.add(Room.Right.SUPER_MODERATOR);
    }
    if (result.isEmpty()) {
        if (!r.isModerated() && 1 == userCount) {
            //room is not moderated, first user is moderator!
            result.add(Room.Right.MODERATOR);
        }
        //performing loop here to set possible 'superModerator' right
        for (RoomModerator rm : r.getModerators()) {
            if (u.getId().equals(rm.getUser().getId())) {
                result.add(rm.isSuperModerator() ? Room.Right.SUPER_MODERATOR : Room.Right.MODERATOR);
                break;
            }
        }
        //no need to loop if client is moderator
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
    if (Room.Type.CONFERENCE == r.getType() && !result.contains(Room.Right.SUPER_MODERATOR) && !result.contains(Room.Right.MODERATOR) && !result.contains(Room.Right.VIDEO)) {
        result.add(Room.Right.AUDIO);
        result.add(Room.Right.VIDEO);
    }
    return result;
}