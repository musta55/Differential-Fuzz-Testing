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
    String text = "";
    final String name = self ? getString("1362") : a.getName();
    final String fmt = ((BasePage) getPage()).isRtl() ? ACTIVITY_FMT_RTL : ACTIVITY_FMT;
    switch(a.getType()) {
        case ROOM_ENTER:
            text = String.format(fmt, name, getString("activities.msg.enter"), df.format(a.getCreated()));
            break;
        case ROOM_EXIT:
            text = String.format(fmt, name, getString("activities.msg.exit"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_MODERATOR:
            text = String.format(fmt, name, getString("activities.request.right.moderator"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_PRESENTER:
            text = String.format(fmt, name, getString("activities.request.right.presenter"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_WB:
            text = String.format(fmt, name, getString("activities.request.right.wb"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_SHARE:
            text = String.format(fmt, name, getString("activities.request.right.share"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_REMOTE:
            text = String.format(fmt, name, getString("activities.request.right.remote"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_A:
            text = String.format(fmt, name, getString("activities.request.right.audio"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_AV:
            text = String.format(fmt, name, getString("activities.request.right.video"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_MUTE_OTHERS:
            text = String.format(fmt, name, getString("activities.request.right.muteothers"), df.format(a.getCreated()));
            break;
        case REQ_RIGHT_HAVE_QUESTION:
            text = String.format(fmt, name, getString("activities.ask.question"), df.format(a.getCreated()));
            break;
        default:
            break;
    }
    final JSONObject aobj = new JSONObject().put("id", a.getId()).put("uid", a.getUid()).put("cssClass", getClass(a)).put("text", text).put("action", a.getType().isAction()).put("find", false);
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
        default:
            break;
    }
    handler.appendJavaScript(new StringBuilder("Activities.add(").append(aobj.toString()).append(");"));
}