public static String displayStatistics() {
    StringBuilder sb = new StringBuilder();
    String blas = NativeHelper.getCurrentBLAS();
    sb.append("Native " + blas + " calls (dense mult/conv/bwdF/bwdD):\t" + numLibMatrixMultCalls.longValue() + "/" + numConv2dCalls.longValue() + "/" + numConv2dBwdFilterCalls.longValue() + "/" + numConv2dBwdDataCalls.longValue() + ".\n");
    sb.append("Native " + blas + " calls (sparse conv/bwdF/bwdD):\t" + numSparseConv2dCalls.longValue() + "/" + numSparseConv2dBwdFilterCalls.longValue() + "/" + numSparseConv2dBwdDataCalls.longValue() + ".\n");
    sb.append("Native " + blas + " times (dense mult/conv/bwdF/bwdD):\t" + String.format("%.3f", libMatrixMultTime.longValue() * 1e-9) + "/" + String.format("%.3f", conv2dTime.longValue() * 1e-9) + "/" + String.format("%.3f", conv2dBwdFilterTime.longValue() * 1e-9) + "/" + String.format("%.3f", conv2dBwdDataTime.longValue() * 1e-9) + ".\n");
    return sb.toString();
}