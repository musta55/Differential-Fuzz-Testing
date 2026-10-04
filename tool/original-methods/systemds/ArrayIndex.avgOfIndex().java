@Override
public double avgOfIndex() {
    double s = 0.0;
    for (int i = 0; i < cols.length; i++) s += cols[i];
    return s / cols.length;
}