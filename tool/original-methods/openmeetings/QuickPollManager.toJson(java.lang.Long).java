public JSONObject toJson(Long roomId) {
    boolean started = isStarted(roomId);
    JSONObject o = new JSONObject().put("started", started);
    if (started) {
        Map<Long, Boolean> votes = map().get(roomId);
        o.put("voted", votes.containsKey(getUserId()));
        o.put("pros", votes.entrySet().stream().filter(Entry::getValue).count()).put("cons", votes.entrySet().stream().filter(e -> !e.getValue()).count());
    }
    return o;
}