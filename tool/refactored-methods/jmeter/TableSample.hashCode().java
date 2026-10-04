@Override
public int hashCode() {
    return Objects.hash(totalSamples, sampleCount, startTime, threadName, label, elapsed, success, bytes, sentBytes, latency, connect);
}