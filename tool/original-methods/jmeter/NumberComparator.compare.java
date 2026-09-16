/**
 * {@inheritDoc}
 */
@Override
public int compare(Number[] n1, Number[] n2) {
    if (n1[0].longValue() < n2[0].longValue()) {
        return -1;
    } else if (n1[0].longValue() == n2[0].longValue()) {
        return 0;
    } else {
        return 1;
    }
}