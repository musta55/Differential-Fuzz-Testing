@Override
public boolean equals(IColIndex other) {
    if (other == this)
        return true;
    else if (size() == other.size()) {
        if (other instanceof CombinedIndex) {
            CombinedIndex o = (CombinedIndex) other;
            return o.l.equals(l) && o.r.equals(r);
        } else {
            IIterate t = iterator();
            IIterate o = other.iterator();
            while (t.hasNext()) {
                if (t.next() != o.next())
                    return false;
            }
            return true;
        }
    }
    return false;
}