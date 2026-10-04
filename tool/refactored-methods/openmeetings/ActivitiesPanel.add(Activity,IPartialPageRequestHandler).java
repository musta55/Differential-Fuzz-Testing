public void add(Activity a, IPartialPageRequestHandler handler) {
    if (!isVisible()) {
        return;
    }
    final boolean self = getUserId().equals(a.getSender());
    if (shouldSkip(self, a)) {
        return;
    }
    if (a.getType().isAction()) {
        remove(handler, activities.entrySet().parallelStream().filter(e -> a.getSender().equals(e.getValue().getSender()) && a.getType() == e.getValue().getType()).map(e -> e.getValue().getId()).toArray(String[]::new));
    }
    activities.put(a.getId(), a);
    String text = formatActivityText(self, a);
    JSONObject aobj = createActivityObject(a, self, text);
    handler.appendJavaScript(new StringBuilder("Activities.add(").append(aobj.toString()).append(");"));
}
// ---- helper method(s) introduced by the refactoring ----
private String formatActivityText(boolean self, Activity a) {
    final String name = self ? getString("1362") : a.getName();
    final String fmt = ((BasePage) getPage()).isRtl() ? ACTIVITY_FMT_RTL : ACTIVITY_FMT;
    return switch(a.getType()) {
        case ROOM_ENTER ->
            String.format(fmt, name, getString("activities.msg.enter"), df.format(a.getCreated()));
        case ROOM_EXIT ->
            String.format(fmt, name, getString("activities.msg.exit"), df.format(a.getCreated()));
        case REQ_RIGHT_MODERATOR ->
            String.format(fmt, name, getString("activities.request.right.moderator"), df.format(a.getCreated()));
        case REQ_RIGHT_PRESENTER ->
            String.format(fmt, name, getString("activities.request.right.presenter"), df.format(a.getCreated()));
        case REQ_RIGHT_WB ->
            String.format(fmt, name, getString("activities.request.right.wb"), df.format(a.getCreated()));
        case REQ_RIGHT_SHARE ->
            String.format(fmt, name, getString("activities.request.right.share"), df.format(a.getCreated()));
        case REQ_RIGHT_REMOTE ->
            String.format(fmt, name, getString("activities.request.right.remote"), df.format(a.getCreated()));
        case REQ_RIGHT_A ->
            String.format(fmt, name, getString("activities.request.right.audio"), df.format(a.getCreated()));
        case REQ_RIGHT_AV ->
            String.format(fmt, name, getString("activities.request.right.video"), df.format(a.getCreated()));
        case REQ_RIGHT_MUTE_OTHERS ->
            String.format(fmt, name, getString("activities.request.right.muteothers"), df.format(a.getCreated()));
        case REQ_RIGHT_HAVE_QUESTION ->
            String.format(fmt, name, getString("activities.ask.question"), df.format(a.getCreated()));
        default ->
            "";
    };
}

private JSONObject createActivityObject(Activity a, boolean self, String text) {
    JSONObject aobj = new JSONObject().put("id", a.getId()).put("uid", a.getUid()).put("cssClass", getActivityClass(a)).put("text", text).put("action", a.getType().isAction()).put("find", false);
    setActivityPermissions(aobj, a, self);
    return aobj;
}

private void setActivityPermissions(JSONObject aobj, Activity a, boolean self) {
    switch(a.getType()) {
        case REQ_RIGHT_MODERATOR, REQ_RIGHT_PRESENTER, REQ_RIGHT_WB, REQ_RIGHT_SHARE, REQ_RIGHT_REMOTE, REQ_RIGHT_A, REQ_RIGHT_AV, REQ_RIGHT_MUTE_OTHERS:
            aobj.put("accept", room.getClient().hasRight(Right.MODERATOR));
            aobj.put("decline", room.getClient().hasRight(Right.MODERATOR));
            break;
        case REQ_RIGHT_HAVE_QUESTION:
            aobj.put("find", !self);
        case ROOM_ENTER, ROOM_EXIT:
            aobj.put("accept", false);
            aobj.put("decline", false);
            break;
    }
}

private static CharSequence getActivityClass(Activity a) {
    return switch(a.getType()) {
        case REQ_RIGHT_MODERATOR, REQ_RIGHT_PRESENTER, REQ_RIGHT_WB, REQ_RIGHT_SHARE, REQ_RIGHT_REMOTE, REQ_RIGHT_A, REQ_RIGHT_AV, REQ_RIGHT_MUTE_OTHERS, REQ_RIGHT_HAVE_QUESTION ->
            "bg-warning";
        case ROOM_ENTER, ROOM_EXIT ->
            "bg-white auto-clean";
        default ->
            "";
    };
}

