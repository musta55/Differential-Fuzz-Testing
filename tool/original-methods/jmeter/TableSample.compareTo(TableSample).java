@Override
public int compareTo(TableSample o) {
    TableSample oo = o;
    return (totalSamples - oo.totalSamples) < 0 ? -1 : (totalSamples == oo.totalSamples ? 0 : 1);
}