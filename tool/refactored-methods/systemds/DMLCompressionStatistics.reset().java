public static void reset() {
    for (int i = 0; i < NUM_PHASES; i++) {
        phaseTimes[i] = 0.0;
    }
    decompressSTCount = 0;
    decompressST = 0.0;
    decompressMTCount = 0;
    decompressMT = 0.0;
    decompressToSTCount = 0;
    decompressToST = 0.0;
    decompressToMTCount = 0;
    decompressToMT = 0.0;
    decompressSparkCount = 0;
    decompressCacheCount = 0;
}