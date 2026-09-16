private void toggleRight(IPartialPageRequestHandler handler, Client self, String uid, JSONObject o) {
    try {
        Right right = Right.valueOf(o.getString(PARAM_RIGHT));
        if (self.hasRight(Right.MODERATOR)) {
            Client client = cm.get(uid);
            if (client == null) {
                return;
            }
            if (client.hasRight(right)) {
                room.denyRight(client, right);
            } else {
                if (Right.VIDEO == right) {
                    room.allowRight(client, Right.AUDIO, right);
                } else {
                    room.allowRight(client, right);
                }
            }
        } else {
            room.requestRight(right, handler);
        }
    } catch (Exception e) {
        log.error("Unexpected exception while toggle 'right'", e);
    }
}