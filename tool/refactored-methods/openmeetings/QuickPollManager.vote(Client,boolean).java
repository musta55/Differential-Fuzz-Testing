public void vote(Client c, boolean vote) {
    Long roomId = c.getRoomId();
    IMap<Long, Map<Long, Boolean>> polls = map();
    polls.lock(roomId);
    try {
        if (polls.containsKey(roomId)) {
            updateVotes(polls.get(roomId), c.getUserId(), vote, roomId, c);
        }
    } finally {
        polls.unlock(roomId);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void updateVotes(Map<Long, Boolean> votes, Long userId, boolean vote, Long roomId, Client c) {
    if (!votes.containsKey(userId)) {
        votes.put(userId, vote);
        map().put(roomId, votes);
        WebSocketHelper.sendRoom(new TextRoomMessage(roomId, c, Type.QUICK_POLL_UPDATED, c.getUid()));
    }
}

