@Override
public boolean equals(IColIndex other) {
    if (other.size() == size()) {
        if (other instanceof ArrayIndex) {
            ArrayIndex ot = (ArrayIndex) other;
            int[] otV = ot.cols;
            return Arrays.equals(cols, otV);
        } else if (other instanceof RangeIndex)
            return other.get(0) == cols[0] && other.get(size() - 1) == cols[size() - 1];
        else {
            // generic
            for (int i = 0; i < size(); i++) if (other.get(i) != cols[i])
                return false;
            return true;
        }
    }
    return false;
}