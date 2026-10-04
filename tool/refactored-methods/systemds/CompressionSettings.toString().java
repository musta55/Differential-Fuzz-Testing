@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("CompressionSettings: ");
    sb.append("\t Valid Compressions: ").append(validCompressions).append("\n");
    sb.append("\t Share dict: ").append(allowSharedDictionary).append("\n");
    sb.append("\t Partitioner: ").append(columnPartitioner).append("\n");
    sb.append("\t Lossy: ").append(lossy).append("\n");
    sb.append("\t Cost Computation Type: ").append(costComputationType).append("\n");
    if (samplingRatio < 1.0)
        sb.append("\t Estimation Type: ").append(estimationType).append("\n");
    return sb.toString();
}