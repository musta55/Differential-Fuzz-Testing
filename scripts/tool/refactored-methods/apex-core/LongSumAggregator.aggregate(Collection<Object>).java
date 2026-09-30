@Override
public Object aggregate(Collection<Object> metricValues) {
    if (metricValues == null) {
        return 0L;
    }
    long sum = 0;
    for (Object value : metricValues) {
        if (value instanceof Number) {
            sum += ((Number) value).longValue();
        }
    }
    return sum;
}