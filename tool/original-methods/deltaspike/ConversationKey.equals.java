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
    if (!groupKey.equals(that.groupKey)) {
        return false;
    }
    if (qualifiers == null && that.qualifiers == null) {
        return true;
    }
    if (qualifiers != null && that.qualifiers == null) {
        return false;
    }
    if (!that.qualifiers.equals(qualifiers)) {
        return false;
    }
    return true;
}