@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("CompressionSettings: ");
    sb.append("\t Valid Compressions: " + validCompressions);
    sb.append("\t Share dict: " + allowSharedDictionary);
    sb.append("\t Partitioner: " + columnPartitioner);
    sb.append("\t Lossy: " + lossy);
    sb.append("\t Cost Computation Type: " + costComputationType);
    if (samplingRatio < 1.0)
        sb.append("\t Estimation Type: " + estimationType);
    return sb.toString();
}