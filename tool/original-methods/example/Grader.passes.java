/**
 * Rejects negatives. The refactored version drops this guard.
 */
public boolean passes(int score) {
    if (score < 0) {
        throw new IllegalArgumentException("negative score");
    }
    return score >= 60;
}