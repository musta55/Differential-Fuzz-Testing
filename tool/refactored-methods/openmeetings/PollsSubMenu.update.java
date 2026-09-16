public void update(final boolean moder, final boolean notExternalUser, final Room r) {
    if (!visible) {
        return;
    }
    boolean pollExists = pollDao.hasPoll(r.getId());
    pollsMenu.setVisible(isPollsMenuVisible(moder, r));
    pollQuickMenuItem.setVisible(isQuickPollMenuItemVisible(r));
    pollCreateMenuItem.setVisible(moder);
    pollVoteMenuItem.setVisible(isPollVoteMenuItemVisible(pollExists, notExternalUser, r));
    pollResultMenuItem.setVisible(isPollResultMenuItemVisible(pollExists, r));
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

