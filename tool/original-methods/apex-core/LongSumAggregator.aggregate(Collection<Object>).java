@Override
public Object aggregate(Collection<Object> metricValues) {
    long sum = 0;
    for (Object value : metricValues) {
        sum += ((Number) value).longValue();
    }
    return sum;
}