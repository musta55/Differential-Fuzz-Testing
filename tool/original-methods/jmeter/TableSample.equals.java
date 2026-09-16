// TODO should equals and hashCode depend on field other than count?
@Override
public boolean equals(Object o) {
    return (o instanceof TableSample) && (this.compareTo((TableSample) o) == 0);
}