public static String encodeAtom(byte[] b, int strt, int left) {
    StringBuilder out = new StringBuilder();
    if (left == 1) {
        byte b1 = b[strt];
        appendEncoded(out, b1 >>> 2 & 63);
        appendEncoded(out, b1 << 4 & 48);
        out.append(EQ).append(EQ);
    } else if (left == 2) {
        byte b2 = b[strt];
        byte b4 = b[strt + 1];
        appendEncoded(out, b2 >>> 2 & 63);
        appendEncoded(out, (b2 << 4 & 48) + (b4 >>> 4 & 15));
        appendEncoded(out, b4 << 2 & 60);
        out.append(EQ);
    } else {
        byte b3 = b[strt];
        byte b5 = b[strt + 1];
        byte b6 = b[strt + 2];
        appendEncoded(out, b3 >>> 2 & 63);
        appendEncoded(out, (b3 << 4 & 48) + (b5 >>> 4 & 15));
        appendEncoded(out, (b5 << 2 & 60) + (b6 >>> 6 & 3));
        appendEncoded(out, b6 & 63);
    }
    return out.toString();
}
// ---- helper method(s) introduced by the refactoring ----
public static String encode(byte[] bs, java.nio.charset.Charset charset) {
    StringBuilder out = new StringBuilder();
    int bl = bs.length;
    for (int i = 0; i < bl; i += 3) {
        out.append(encodeAtom(bs, i, bl - i));
    }
    return out.toString();
}

private static void appendEncoded(StringBuilder out, int index) {
    out.append(PEM_ARRAY[index]);
}

