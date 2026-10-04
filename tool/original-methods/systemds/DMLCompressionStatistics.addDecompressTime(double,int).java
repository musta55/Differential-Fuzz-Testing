public static void addDecompressTime(double time, int threads) {
    if (threads == 1) {
        DecompressSTCount++;
        DecompressST += time;
    } else {
        DecompressMTCount++;
        DecompressMT += time;
    }
}