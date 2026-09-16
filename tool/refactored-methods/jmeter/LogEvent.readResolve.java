/**
 * Helper method that replaces deserialized priority with correct singleton.
 *
 * @return the singleton version of object
 * @exception ObjectStreamException if an error occurs
 */
private Object readResolve() throws ObjectStreamException {
    initializeFields();
    restorePriority();
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeFields() {
    if (null == m_category) {
        m_category = "";
    }
    if (null == m_message) {
        m_message = "";
    }
}

private void restorePriority() {
    String priorityName = (m_priority != null) ? m_priority.getName() : "";
    m_priority = Priority.getPriorityForName(priorityName);
}

