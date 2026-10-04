public static void addCompressionTime(double time, int phase) {
    if (phase >= 0 && phase < NUM_PHASES) {
        phaseTimes[phase] += time;
    }
}