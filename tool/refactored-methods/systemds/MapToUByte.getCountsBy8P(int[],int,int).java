private void getCountsBy8P(int[] ret, int s, int e) {
    for (int i = s; i < e; i += 8) incrementCounts(ret, i);
}
// ---- helper method(s) introduced by the refactoring ----
private void incrementCounts(int[] ret, int index) {
    for (int i = 0; i < 8; i++) ret[_data[index + i]]++;
}

