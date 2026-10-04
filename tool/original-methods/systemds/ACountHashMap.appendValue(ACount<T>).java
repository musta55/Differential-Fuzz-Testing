protected void appendValue(ACount<T> ent) {
    if (ent != null) {
        // take the tail recursively first
        // append tail first
        appendValue(ent.next());
        // set this tail to null.
        ent.setNext(null);
        final int ix = hash(ent.key()) % data.length;
        try {
            appendValue(ent, ix);
        } catch (ArrayIndexOutOfBoundsException e) {
            if (ix < 0)
                appendValue(ent, 0);
            else
                throw new RuntimeException(e);
        }
    }
}