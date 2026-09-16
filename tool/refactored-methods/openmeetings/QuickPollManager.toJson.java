public JSONObject toJson(Long roomId) {
    boolean started = isStarted(roomId);
    JSONObject o = new JSONObject().put("started", started);
    if (started) {
        Map<Long, Boolean> votes = map().get(roomId);
        o.put("voted", votes.containsKey(getUserId())).put("pros", votes.entrySet().stream().filter(Entry::getValue).count()).put("cons", votes.entrySet().stream().filter(e -> !e.getValue()).count());
    }
    return o;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateVotes(Map<Long, Boolean> votes, Long userId, boolean vote, Long roomId, Client c) {
    if (!votes.containsKey(userId)) {
        votes.put(userId, vote);
        map().put(roomId, votes);
        WebSocketHelper.sendRoom(new TextRoomMessage(roomId, c, Type.QUICK_POLL_UPDATED, c.getUid()));
    }
}

