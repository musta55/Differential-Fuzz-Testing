/**
 * {@inheritDoc}
 */
@Override
public int hashCode() {
    int result = groupKey.hashCode();
    result = 31 * result + qualifiers.hashCode();
    return result;
}