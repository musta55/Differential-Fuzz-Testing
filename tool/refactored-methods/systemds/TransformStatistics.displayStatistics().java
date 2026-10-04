public static String displayStatistics() {
    if (encoderCount.longValue() > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append("TransformEncode num. encoders:\t").append(encoderCount.longValue()).append("\n");
        sb.append("TransformEncode build time:\t").append(formatTime(getEncodeBuildTime())).append(" sec.\n");
        appendIfPositive(sb, "Recode build time:\t", recodeBuildTime);
        appendIfPositive(sb, "Binning build time:\t", binningBuildTime);
        appendIfPositive(sb, "Impute build time:\t", imputeBuildTime);
        appendIfPositive(sb, "BagOfWords build time:\t", bowBuildTime);
        sb.append("TransformEncode apply time:\t").append(formatTime(getEncodeApplyTime())).append(" sec.\n");
        appendIfPositive(sb, "Recode apply time:\t", recodeApplyTime);
        appendIfPositive(sb, "Binning apply time:\t", binningApplyTime);
        appendIfPositive(sb, "DummyCode apply time:\t", dummyCodeApplyTime);
        appendIfPositive(sb, "WordEmbedding apply time:\t", wordEmbeddingApplyTime);
        appendIfPositive(sb, "BagOfWords apply time:\t", bagOfWordsApplyTime);
        appendIfPositive(sb, "Hashing apply time:\t", featureHashingApplyTime);
        appendIfPositive(sb, "PassThrough apply time:\t", passThroughApplyTime);
        appendIfPositive(sb, "UDF apply time:\t", UDFApplyTime);
        appendIfPositive(sb, "Omit apply time:\t", omitApplyTime);
        appendIfPositive(sb, "Impute apply time:\t", imputeApplyTime);
        sb.append("TransformEncode PreProc. time:\t").append(formatTime(outMatrixPreProcessingTime.longValue())).append(" sec.\n");
        sb.append("TransformEncode PostProc. time:\t").append(formatTime(outMatrixPostProcessingTime.longValue())).append(" sec.\n");
        appendIfPositive(sb, "TransformEncode SizeEst. time:\t", mapSizeEstimationTime);
        return sb.toString();
    }
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfPositive(StringBuilder sb, String label, LongAdder time) {
    if (time.longValue() > 0) {
        sb.append(label).append(formatTime(time.longValue())).append(" sec.\n");
    }
}

private static String formatTime(long nanos) {
    return String.format("%.3f", nanos * 1e-9);
}

