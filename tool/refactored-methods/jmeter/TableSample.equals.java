// TODO should equals and hashCode depend on field other than count?
@Override
public boolean equals(Object o) {
    if (this == o)
        return true;
    if (!(o instanceof TableSample))
        return false;
    TableSample that = (TableSample) o;
    return totalSamples == that.totalSamples && sampleCount == that.sampleCount && startTime == that.startTime && elapsed == that.elapsed && success == that.success && bytes == that.bytes && sentBytes == that.sentBytes && latency == that.latency && connect == that.connect && Objects.equals(threadName, that.threadName) && Objects.equals(label, that.label);
}