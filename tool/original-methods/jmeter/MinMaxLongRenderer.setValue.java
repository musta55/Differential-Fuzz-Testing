@Override
public void setValue(Object value) {
    if (value instanceof Long) {
        long longValue = (Long) value;
        if (!(longValue == Long.MAX_VALUE || longValue == Long.MIN_VALUE)) {
            setText(formatter.format(longValue));
            return;
        }
    }
    setText("#N/A");
}