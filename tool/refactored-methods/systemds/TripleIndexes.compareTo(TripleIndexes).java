@Override
public int compareTo(TripleIndexes other) {
    if (this.first != other.first)
        return Long.compare(this.first, other.first);
    else if (this.second != other.second)
        return Long.compare(this.second, other.second);
    else if (this.third != other.third)
        return Long.compare(this.third, other.third);
    return 0;
}