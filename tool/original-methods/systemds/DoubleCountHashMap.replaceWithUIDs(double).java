public void replaceWithUIDs(double v) {
    int i = 0;
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