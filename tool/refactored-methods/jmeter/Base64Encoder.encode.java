public static String encode(byte[] bs) {
    return encode(bs, java.nio.charset.StandardCharsets.UTF_8);
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

