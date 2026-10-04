public void updatePoll(IPartialPageRequestHandler handler, Long createdBy) {
    RoomPoll rp = pollDao.getByRoom(room.getRoom().getId());
    if (rp != null) {
        vote.updateModel(handler, rp);
    } else {
        vote.close(handler);
    }
    if (shouldShowVoteDialog(createdBy)) {
        vote.show(handler);
    }
    if (pollResults.isOpened()) {
        pollResults.updateModel(handler, false, room.getClient().hasRight(Room.Right.MODERATOR));
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isPollMenuHidden() {
    return room.getRoom().isHidden(RoomElement.POLL_MENU);
}

private String getActionFromRequest() {
    return mp.getRequest().getRequestParameters().getParameterValue(PARAM_ACTION).toString();
}

private void handleAction(String action, Client c, AjaxRequestTarget target) {
    switch(action) {
        case ACTION_OPEN:
            qpollManager.start(c);
            break;
        case ACTION_CLOSE:
            qpollManager.close(c);
            break;
        case PARAM_VOTE:
            boolean curVote = mp.getRequest().getRequestParameters().getParameterValue(PARAM_VOTE).toBoolean();
            qpollManager.vote(c, curVote);
            break;
        default:
            break;
    }
}

private boolean isPollsMenuVisible(boolean moder, Room r) {
    return moder || r.isAllowUserQuestions();
}

private boolean isQuickPollMenuItemVisible(Room r) {
    return room.getClient().hasRight(Room.Right.PRESENTER) && !qpollManager.isStarted(r.getId());
}

private boolean isPollVoteMenuItemVisible(boolean pollExists, boolean notExternalUser, Room r) {
    return pollExists && notExternalUser && pollDao.notVoted(r.getId(), getUserId());
}

private boolean isPollResultMenuItemVisible(boolean pollExists, Room r) {
    return pollExists || !pollDao.getArchived(r.getId()).isEmpty();
}

private boolean shouldShowVoteDialog(Long createdBy) {
    return createdBy != null && !getUserId().equals(createdBy);
}

