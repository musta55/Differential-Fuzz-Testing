@Override
public int hashCode() {
    return (int) (totalSamples ^ (totalSamples >>> 32));
}