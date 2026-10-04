@Override
public Double getResult() {
    if (sum == null) {
        return null;
    }
    return sum / count;
}