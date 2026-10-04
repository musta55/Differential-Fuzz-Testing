public void remove(ColIndexes c1, ColIndexes c2) {
    Iterator<Entry<ColIndexes, CompressedSizeInfoColGroup>> i = mem.entrySet().iterator();
    while (i.hasNext()) {
        final ColIndexes eci = i.next().getKey();
        if (eci.contains(c1, c2))
            i.remove();
    }
}