public void update(final boolean moder, final boolean notExternalUser, final Room r) {
    if (!visible) {
        return;
    }
    boolean pollExists = pollDao.hasPoll(r.getId());
    pollsMenu.setVisible(moder || r.isAllowUserQuestions());
    pollQuickMenuItem.setVisible(room.getClient().hasRight(Room.Right.PRESENTER) && !qpollManager.isStarted(r.getId()));
    pollCreateMenuItem.setVisible(moder);
    pollVoteMenuItem.setVisible(pollExists && notExternalUser && pollDao.notVoted(r.getId(), getUserId()));
    pollResultMenuItem.setVisible(pollExists || !pollDao.getArchived(r.getId()).isEmpty());
}