@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    if (scans > 0)
        sb.append(String.format("Sca:%d;", scans));
    if (decompressions > 0)
        sb.append(String.format("DeC:%d;", decompressions));
    if (overlappingDecompressions > 0)
        sb.append(String.format("OvD:%d;", overlappingDecompressions));
    if (leftMultiplications > 0)
        sb.append(String.format("LMM:%d;", leftMultiplications));
    if (rightMultiplications > 0)
        sb.append(String.format("RMM:%d;", rightMultiplications));
    if (compressedMultiplications > 0)
        sb.append(String.format("CMM:%d;", compressedMultiplications));
    if (dictionaryOps > 0)
        sb.append(String.format("dic:%d;", dictionaryOps));
    if (indexing > 0)
        sb.append(String.format("ind:%d;", indexing));
    if (sb.length() > 1)
        // remove last semicolon
        sb.setLength(sb.length() - 1);
    return sb.toString();
}