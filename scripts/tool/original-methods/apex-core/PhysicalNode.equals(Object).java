/**
 * @param o
 * @return boolean
 */
@Override
public boolean equals(Object o) {
    return o == this || (o.getClass() == this.getClass() && o.hashCode() == this.hashCode());
}