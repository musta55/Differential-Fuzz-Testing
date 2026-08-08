package example;

import java.util.List;

/**
 * Refactored counterpart of the branchy demo class. See the original for what each method is for.
 */
public class Grader {

    /** Same partition, tested from the other end — equivalent for every int. */
    public String grade(int score) {
        if (score < 70) {
            return "F";
        } else if (score < 80) {
            return "C";
        } else if (score < 90) {
            return "B";
        } else {
            return "A";
        }
    }

    /** The negative guard is gone: returns false where the original threw. */
    public boolean passes(int score) {
        return score >= 60;
    }

    /** Index loop instead of the for-each, skipping nulls the other way round — equivalent. */
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
}
