/**
 * Helper method that replaces deserialized priority with correct singleton.
 *
 * @return the singleton version of object
 * @exception ObjectStreamException if an error occurs
 */
private Object readResolve() throws ObjectStreamException {
    if (null == m_category) {
        m_category = "";
    }
    if (null == m_message) {
        m_message = "";
    }
    String priorityName = "";
    if (null != m_priority) {
        priorityName = m_priority.getName();
    }
    m_priority = Priority.getPriorityForName(priorityName);
    return this;
}