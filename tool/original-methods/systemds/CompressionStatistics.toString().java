@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nCompressionStatistics:");
    sb.append("\nDense Size            : " + denseSize);
    sb.append("\nOriginal Size         : " + originalSize);
    sb.append("\nCompressed Size       : " + compressedSize);
    sb.append("\nCompressionRatio      : " + getRatio());
    sb.append("\nDenseCompressionRatio : " + getDenseRatio());
    if (colGroupCounts != null) {
        sb.append("\nCompressionTypes      : " + getGroupsTypesString());
        sb.append("\nCompressionGroupSizes : " + getGroupsSizesString());
    }
    return sb.toString();
}