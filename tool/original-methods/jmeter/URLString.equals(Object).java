/**
 * {@inheritDoc}
 */
@Override
public boolean equals(Object o) {
    return o instanceof URLString && urlAsString.equals(o.toString());
}