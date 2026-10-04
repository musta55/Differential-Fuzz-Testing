/**
 * @return the answers
 */
public List<RoomPollAnswer> getAnswers() {
    if (answers == null) {
        answers = new LinkedList<>();
    }
    return answers;
}