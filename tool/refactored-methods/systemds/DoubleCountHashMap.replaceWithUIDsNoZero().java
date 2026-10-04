public void replaceWithUIDsNoZero() {
    int i = 0;
    Double z = Double.valueOf(0.0);
    replaceWithUIDs(z, i);
}
// ---- helper method(s) introduced by the refactoring ----
private void populateDictionary(double[] ret) {
    for (int i = 0; i < data.length; i++) {
        ACount<Double> e = data[i];
        while (e != null) {
            if (e.id >= 0)
                ret[e.id] = e.key();
            e = e.next();
        }
    }
}

private void replaceWithUIDs(double v, int i) {
    for (ACount<Double> e : data) {
        while (e != null) {
            if (!e.key().equals(v))
                e.id = i++;
            else
                e.id = -1;
            e = e.next();
        }
    }
}

