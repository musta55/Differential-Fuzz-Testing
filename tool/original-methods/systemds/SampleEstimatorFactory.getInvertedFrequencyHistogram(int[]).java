public static int[] getInvertedFrequencyHistogram(int[] frequencies) {
    try {
        final int numVals = frequencies.length;
        // Find max
        int maxCount = 0;
        for (int i = 0; i < numVals; i++) {
            final int v = frequencies[i];
            if (v > maxCount)
                maxCount = v;
        }
        // create frequency histogram
        int[] freqCounts = new int[maxCount];
        for (int i = 0; i < numVals; i++) freqCounts[frequencies[i] - 1]++;
        return freqCounts;
    } catch (Exception e) {
        throw new RuntimeException(Arrays.toString(frequencies), e);
    }
}