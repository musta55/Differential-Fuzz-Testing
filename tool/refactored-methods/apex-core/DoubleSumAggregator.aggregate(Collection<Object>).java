@Override
public Object aggregate(Collection<Object> metricValues) {
    if (metricValues == null) {
        return 0.0;
    }
    double sum = 0;
    for (Object value : metricValues) {
        if (value instanceof Number) {
            sum += ((Number) value).doubleValue();
        }
    }
    return sum;
}