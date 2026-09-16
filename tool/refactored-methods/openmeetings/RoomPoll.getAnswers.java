/**
 * @return the answers
 */
public List<RoomPollAnswer> getAnswers() {
    if (answers == null) {
        initAnswers();
    }
    return answers;
}
// ---- helper method(s) introduced by the refactoring ----
private synchronized void initAnswers() {
    if (answers == null) {
        answers = new LinkedList<>();
    }
}

