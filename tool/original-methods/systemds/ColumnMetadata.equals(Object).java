@Override
public boolean equals(Object that) {
    return that instanceof ColumnMetadata ? equals((ColumnMetadata) that) : false;
}