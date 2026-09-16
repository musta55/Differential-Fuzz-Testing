public void roomAction(IPartialPageRequestHandler handler, JSONObject o) {
    try {
        final String uid = o.getString(PARAM_UID);
        if (Strings.isEmpty(uid)) {
            return;
        }
        Client self = room.getClient();
        Action a = Action.of(o.getString(PARAM_ACTION));
        switch(a) {
            case KICK:
                if (self.hasRight(Right.MODERATOR)) {
                    final Client kickedClient = cm.get(uid);
                    if (kickedClient == null) {
                        return;
                    }
                    if (!kickedClient.hasRight(Right.SUPER_MODERATOR) && !self.getUid().equals(kickedClient.getUid())) {
                        kickUser(kickedClient);
                    }
                }
                break;
            case MUTE_OTHERS:
                if (room.getClient().hasRight(Right.MUTE_OTHERS)) {
                    WebSocketHelper.sendRoom(new TextRoomMessage(room.getRoom().getId(), self, RoomMessage.Type.MUTE_OTHERS, uid));
                }
                break;
            case MUTE:
                muteRoomAction(uid, self, o);
                break;
            case TOGGLE_RIGHT:
                toggleRight(handler, self, uid, o);
                break;
            default:
        }
    } catch (Exception e) {
        log.error("Unexpected exception while toggle 'roomAction'", e);
    }
}