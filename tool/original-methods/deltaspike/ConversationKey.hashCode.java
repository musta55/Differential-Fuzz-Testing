/**
 * {@inheritDoc}
 */
@Override
public int hashCode() {
    int result = groupKey.hashCode();
    result = 31 * result + (qualifiers != null ? qualifiers.hashCode() : 0);
    return result;
}