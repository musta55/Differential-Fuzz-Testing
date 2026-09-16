/**
 * Same partition, tested from the other end — equivalent for every int.
 */
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