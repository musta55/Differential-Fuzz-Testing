public void vote(Client c, boolean vote) {
    Long roomId = c.getRoomId();
    IMap<Long, Map<Long, Boolean>> polls = map();
    polls.lock(roomId);
    if (polls.containsKey(roomId)) {
        Map<Long, Boolean> votes = map().get(roomId);
        if (!votes.containsKey(c.getUserId())) {
            votes.put(c.getUserId(), vote);
            polls.put(roomId, votes);
            WebSocketHelper.sendRoom(new TextRoomMessage(roomId, c, Type.QUICK_POLL_UPDATED, c.getUid()));
        }
    }
    polls.unlock(roomId);
}