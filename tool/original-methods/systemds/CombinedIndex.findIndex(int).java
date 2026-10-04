@Override
public int findIndex(int i) {
    final int a = l.findIndex(i);
    if (a < 0) {
        final int b = r.findIndex(i);
        if (b < 0)
            return b + a + 1;
        else
            return b + l.size();
    } else
        return a;
}