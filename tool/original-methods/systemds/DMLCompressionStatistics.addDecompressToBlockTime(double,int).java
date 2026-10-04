public static void addDecompressToBlockTime(double time, int threads) {
    if (threads == 1) {
        DecompressToSTCount++;
        DecompressToST += time;
    } else {
        DecompressToMTCount++;
        DecompressToMT += time;
    }
}