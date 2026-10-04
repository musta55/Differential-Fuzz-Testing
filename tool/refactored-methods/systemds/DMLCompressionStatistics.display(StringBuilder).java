public static void display(StringBuilder sb) {
    if (haveCompressed()) {
        // If compression have been used
        sb.append(String.format("CLA Compression Phases :\t%.3f/%.3f/%.3f/%.3f/%.3f/%.3f\n", phaseTimes[0] / 1000, phaseTimes[1] / 1000, phaseTimes[2] / 1000, phaseTimes[3] / 1000, phaseTimes[4] / 1000, phaseTimes[5] / 1000));
        sb.append(String.format("Decompression with allocation (Single, Multi, Spark, Cache) : %d/%d/%d/%d\n", decompressSTCount, decompressMTCount, decompressSparkCount, decompressCacheCount));
        sb.append(String.format("Decompression with allocation Time (Single , Multi)         : %.3f/%.3f sec.\n", decompressST / 1000, decompressMT / 1000));
        sb.append(String.format("Decompression to block (Single, Multi)                      : %d/%d\n", decompressToSTCount, decompressToMTCount));
        sb.append(String.format("Decompression to block Time (Single, Multi)                 : %.3f/%.3f sec.\n", decompressToST / 1000, decompressToMT / 1000));
    }
}