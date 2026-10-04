@Override
public boolean equals(Object other) {
    if (!(other instanceof TripleIndexes))
        return false;
    TripleIndexes tother = (TripleIndexes) other;
    return (this.first == tother.first && this.second == tother.second && this.third == tother.third);
}