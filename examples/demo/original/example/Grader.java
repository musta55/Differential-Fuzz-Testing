package example;

import java.util.List;

/**
 * The branchy half of the demo. Widget and Simple are one-liners, so every Branch column in the
 * report reads 0/0 and the Confidence column has nothing to say — which hides the one thing an
 * EQUIVALENT verdict needs, namely how much of the method the fuzzer actually reached.
 *
 * Each method here exists to produce a different row in that report:
 *   grade   — equivalent refactoring over a 4-way branch  -> EQUIVALENT at high branch coverage
 *   passes  — a dropped guard                             -> DIVERGENT by exception type
 *   total   — a non-primitive (List) parameter            -> exercises the object-building path
 */
public class Grader {

    public String grade(int score) {
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        return "F";
    }

    /** Rejects negatives. The refactored version drops this guard. */
    public boolean passes(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("negative score");
        }
        return score >= 60;
    }

    /** Non-primitive parameter: the engine has to build a List before it can call this. */
    public int total(List<Integer> scores) {
        int sum = 0;
        for (Integer s : scores) {
            if (s != null) {
                sum += s;
            }
        }
        return sum;
    }
}
