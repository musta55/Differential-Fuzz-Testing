public static void addDecompressTime(double time, int threads) {
    if (threads == 1) {
        decompressSTCount++;
        decompressST += time;
    } else {
        decompressMTCount++;
        decompressMT += time;
    }
}