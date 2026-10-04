private static long getTotalFetchTime() {
    return times.stream().filter(t -> t.is("PrivFetch")).map(TimeEntry::getDuration).reduce(0L, Long::sum);
}