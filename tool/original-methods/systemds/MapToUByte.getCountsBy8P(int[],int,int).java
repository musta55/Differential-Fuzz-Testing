private void getCountsBy8P(int[] ret, int s, int e) {
    for (int i = s; i < e; i += 8) {
        ret[_data[i]]++;
        ret[_data[i + 1]]++;
        ret[_data[i + 2]]++;
        ret[_data[i + 3]]++;
        ret[_data[i + 4]]++;
        ret[_data[i + 5]]++;
        ret[_data[i + 6]]++;
        ret[_data[i + 7]]++;
    }
}