/**
 * Index loop instead of the for-each, skipping nulls the other way round — equivalent.
 */
public int total(List<Integer> scores) {
    int sum = 0;
    for (int i = 0; i < scores.size(); i++) {
        Integer s = scores.get(i);
        if (s == null) {
            continue;
        }
        sum += s;
    }
    return sum;
}