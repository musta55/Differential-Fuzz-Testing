public static String displayStatistics() {
    if (encoderCount.longValue() > 0) {
        //TODO: Cleanup and condense
        StringBuilder sb = new StringBuilder();
        sb.append("TransformEncode num. encoders:\t").append(encoderCount.longValue()).append("\n");
        sb.append("TransformEncode build time:\t").append(String.format("%.3f", getEncodeBuildTime() * 1e-9)).append(" sec.\n");
        if (recodeBuildTime.longValue() > 0)
            sb.append("\tRecode build time:\t").append(String.format("%.3f", recodeBuildTime.longValue() * 1e-9)).append(" sec.\n");
        if (binningBuildTime.longValue() > 0)
            sb.append("\tBinning build time:\t").append(String.format("%.3f", binningBuildTime.longValue() * 1e-9)).append(" sec.\n");
        if (imputeBuildTime.longValue() > 0)
            sb.append("\tImpute build time:\t").append(String.format("%.3f", imputeBuildTime.longValue() * 1e-9)).append(" sec.\n");
        if (bowBuildTime.longValue() > 0)
            sb.append("\tBagOfWords build time:\t").append(String.format("%.3f", bowBuildTime.longValue() * 1e-9)).append(" sec.\n");
        sb.append("TransformEncode apply time:\t").append(String.format("%.3f", getEncodeApplyTime() * 1e-9)).append(" sec.\n");
        if (recodeApplyTime.longValue() > 0)
            sb.append("\tRecode apply time:\t").append(String.format("%.3f", recodeApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (binningApplyTime.longValue() > 0)
            sb.append("\tBinning apply time:\t").append(String.format("%.3f", binningApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (dummyCodeApplyTime.longValue() > 0)
            sb.append("\tDummyCode apply time:\t").append(String.format("%.3f", dummyCodeApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (wordEmbeddingApplyTime.longValue() > 0)
            sb.append("\tWordEmbedding apply time:\t").append(String.format("%.3f", wordEmbeddingApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (bagOfWordsApplyTime.longValue() > 0)
            sb.append("\tBagOfWords apply time:\t").append(String.format("%.3f", bagOfWordsApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (featureHashingApplyTime.longValue() > 0)
            sb.append("\tHashing apply time:\t").append(String.format("%.3f", featureHashingApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (passThroughApplyTime.longValue() > 0)
            sb.append("\tPassThrough apply time:\t").append(String.format("%.3f", passThroughApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (UDFApplyTime.longValue() > 0)
            sb.append("\tUDF apply time:\t").append(String.format("%.3f", UDFApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (omitApplyTime.longValue() > 0)
            sb.append("\tOmit apply time:\t").append(String.format("%.3f", omitApplyTime.longValue() * 1e-9)).append(" sec.\n");
        if (imputeApplyTime.longValue() > 0)
            sb.append("\tImpute apply time:\t").append(String.format("%.3f", imputeApplyTime.longValue() * 1e-9)).append(" sec.\n");
        sb.append("TransformEncode PreProc. time:\t").append(String.format("%.3f", outMatrixPreProcessingTime.longValue() * 1e-9)).append(" sec.\n");
        sb.append("TransformEncode PostProc. time:\t").append(String.format("%.3f", outMatrixPostProcessingTime.longValue() * 1e-9)).append(" sec.\n");
        if (mapSizeEstimationTime.longValue() > 0)
            sb.append("TransformEncode SizeEst. time:\t").append(String.format("%.3f", mapSizeEstimationTime.longValue() * 1e-9)).append(" sec.\n");
        return sb.toString();
    }
    return "";
}