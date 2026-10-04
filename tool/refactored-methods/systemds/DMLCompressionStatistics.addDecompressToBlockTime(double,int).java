public static void addDecompressToBlockTime(double time, int threads) {
    if (threads == 1) {
        decompressToSTCount++;
        decompressToST += time;
    } else {
        decompressToMTCount++;
        decompressToMT += time;
    }
}