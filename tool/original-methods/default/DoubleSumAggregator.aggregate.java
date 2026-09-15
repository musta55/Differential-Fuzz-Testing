@Override
public Object aggregate(Collection<Object> metricValues) {
    double sum = 0;
    for (Object value : metricValues) {
        sum += ((Number) value).doubleValue();
    }
    return sum;
}