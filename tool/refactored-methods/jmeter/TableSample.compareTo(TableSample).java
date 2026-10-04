@Override
public int compareTo(TableSample o) {
    return Long.compare(this.totalSamples, o.totalSamples);
}