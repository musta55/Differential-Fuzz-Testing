public static int[] getInvertedFrequencyHistogram(int[] frequencies) {
    int numVals = frequencies.length;
    int maxCount = findMaxCount(frequencies);
    return createFrequencyHistogram(frequencies, maxCount);
}
// ---- helper method(s) introduced by the refactoring ----
private static int findMaxCount(int[] frequencies) {
    int maxCount = 0;
    for (int v : frequencies) {
        if (v > maxCount) {
            maxCount = v;
        }
    }
    return maxCount;
}

private static int[] createFrequencyHistogram(int[] frequencies, int maxCount) {
    int[] freqCounts = new int[maxCount];
    for (int v : frequencies) {
        freqCounts[v - 1]++;
    }
    return freqCounts;
}

