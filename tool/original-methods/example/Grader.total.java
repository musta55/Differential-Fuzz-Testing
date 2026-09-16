/**
 * Non-primitive parameter: the engine has to build a List before it can call this.
 */
public int total(List<Integer> scores) {
    int sum = 0;
    for (Integer s : scores) {
        if (s != null) {
            sum += s;
        }
    }
    return sum;
}