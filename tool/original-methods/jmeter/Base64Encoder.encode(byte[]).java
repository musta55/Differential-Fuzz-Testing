public static String encode(byte[] bs) {
    StringBuilder out = new StringBuilder();
    int bl = bs.length;
    for (int i = 0; i < bl; i += 3) {
        out.append(encodeAtom(bs, i, bl - i));
    }
    return out.toString();
}