/**
 * The negative guard is gone: returns false where the original threw.
 */
public boolean passes(int score) {
    return score >= 60;
}