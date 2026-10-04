public static int getDecompressionCount() {
    return DecompressMTCount + DecompressSTCount + DecompressSparkCount + DecompressCacheCount + DecompressToSTCount + DecompressToMTCount;
}