/**
 * {@inheritDoc}
 */
@Override
public int compare(Number[] n1, Number[] n2) {
    return Long.compare(n1[0].longValue(), n2[0].longValue());
}