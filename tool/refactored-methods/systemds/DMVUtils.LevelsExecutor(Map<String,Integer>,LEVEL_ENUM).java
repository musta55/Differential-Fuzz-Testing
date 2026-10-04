public static Map<String, Integer> LevelsExecutor(Map<String, Integer> old_pattern_hist, LEVEL_ENUM level) {
    Map<String, Integer> new_pattern_hist = new HashMap<>();
    LevelStrategy strategy = levelStrategies.get(level);
    if (strategy == null) {
        throw new IllegalArgumentException("Unknown level: " + level);
    }
    for (Entry<String, Integer> e : old_pattern_hist.entrySet()) {
        String pattern = e.getKey();
        Integer nr_of_occurrences = e.getValue();
        String new_pattern = strategy.apply(pattern);
        addDistinctValueOrIncrementCounter(new_pattern_hist, new_pattern, nr_of_occurrences);
    }
    return new_pattern_hist;
}