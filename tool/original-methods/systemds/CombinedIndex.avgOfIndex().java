@Override
public double avgOfIndex() {
    double lv = l.avgOfIndex() * l.size();
    double rv = r.avgOfIndex() * r.size();
    return (lv + rv) / size();
}