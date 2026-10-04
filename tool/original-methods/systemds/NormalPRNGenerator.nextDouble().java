@Override
public double nextDouble() {
    double d;
    if (!flag) {
        d = pair.getFirst();
    } else {
        d = pair.getSecond();
        pair.compute(rnorm);
    }
    flag = !flag;
    return d;
}