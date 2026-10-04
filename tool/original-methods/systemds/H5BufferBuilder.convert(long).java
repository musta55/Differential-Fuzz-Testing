public static BitSet convert(long value) {
    BitSet bits = new BitSet();
    String a = "";
    int index = 0;
    while (value != 0L) {
        if (value % 2L != 0) {
            bits.set(index);
            a += "1";
        } else
            a += "0";
        ++index;
        value = value >>> 1;
    }
    System.out.println(a);
    return bits;
}