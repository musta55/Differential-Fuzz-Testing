public void replaceWithUIDsNoZero() {
    int i = 0;
    Double z = Double.valueOf(0.0);
    for (ACount<Double> e : data) {
        while (e != null) {
            if (!e.key().equals(z))
                e.id = i++;
            else
                e.id = -1;
            e = e.next();
        }
    }
}