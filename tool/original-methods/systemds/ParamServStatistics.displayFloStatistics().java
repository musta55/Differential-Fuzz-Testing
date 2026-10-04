public static String displayFloStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("PS fed network time (cum):\t%.3f secs.\n", fedNetworkTime.doubleValue() / 1000));
    sb.append(String.format("PS fed agg time:\t%.3f secs.\n", fedAggregation.doubleValue() / 1000));
    sb.append(String.format("Paramserv grad compute time:\t%.3f secs.\n", gradientComputeTime.doubleValue() / 1000));
    sb.append(String.format("HE PS encryption time:\t%.3f secs.\n", heEncryption.doubleValue() / 1000));
    sb.append(String.format("HE PS accumulation time:\t%.3f secs.\n", heAccumulation.doubleValue() / 1000));
    sb.append(String.format("HE PS partial decryption time:\t%.3f secs.\n", hePartialDecryption.doubleValue() / 1000));
    sb.append(String.format("HE PS decryption time:\t%.3f secs.\n", heDecryption.doubleValue() / 1000));
    return sb.toString();
}