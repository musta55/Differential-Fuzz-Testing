@Override
public int compareTo(TripleIndexes other) {
    if (this.first != other.first)
        return (this.first > other.first ? 1 : -1);
    else if (this.second != other.second)
        return (this.second > other.second ? 1 : -1);
    else if (this.third != other.third)
        return (this.third > other.third ? 1 : -1);
    return 0;
}