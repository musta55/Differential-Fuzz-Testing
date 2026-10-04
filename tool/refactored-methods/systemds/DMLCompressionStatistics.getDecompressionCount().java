public static int getDecompressionCount() {
    return decompressMTCount + decompressSTCount + decompressSparkCount + decompressCacheCount + decompressToSTCount + decompressToMTCount;
}