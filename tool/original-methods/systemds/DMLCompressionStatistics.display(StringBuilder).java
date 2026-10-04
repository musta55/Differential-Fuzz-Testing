public static void display(StringBuilder sb) {
    if (haveCompressed()) {
        // If compression have been used
        sb.append(String.format("CLA Compression Phases :\t%.3f/%.3f/%.3f/%.3f/%.3f/%.3f\n", Phase0 / 1000, Phase1 / 1000, Phase2 / 1000, Phase3 / 1000, Phase4 / 1000, Phase5 / 1000));
        sb.append(String.format("Decompression with allocation (Single, Multi, Spark, Cache) : %d/%d/%d/%d\n", DecompressSTCount, DecompressMTCount, DecompressSparkCount, DecompressCacheCount));
        sb.append(String.format("Decompression with allocation Time (Single , Multi)         : %.3f/%.3f sec.\n", DecompressST / 1000, DecompressMT / 1000));
        sb.append(String.format("Decompression to block (Single, Multi)                      : %d/%d\n", DecompressToSTCount, DecompressToMTCount));
        sb.append(String.format("Decompression to block Time (Single, Multi)                 : %.3f/%.3f sec.\n", DecompressToST / 1000, DecompressToMT / 1000));
    }
}