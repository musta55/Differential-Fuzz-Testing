/**
 * {@inheritDoc}
 */
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (!(o instanceof ConversationKey)) {
        return false;
    }
    ConversationKey that = (ConversationKey) o;
    return Objects.equals(groupKey, that.groupKey) && Objects.equals(qualifiers, that.qualifiers);
}