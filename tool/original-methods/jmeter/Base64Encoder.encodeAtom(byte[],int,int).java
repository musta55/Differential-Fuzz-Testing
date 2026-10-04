public static String encodeAtom(byte[] b, int strt, int left) {
    StringBuilder out = new StringBuilder();
    if (left == 1) {
        byte b1 = b[strt];
        int k = 0;
        out.append(String.valueOf(pem_array[b1 >>> 2 & 63]));
        out.append(String.valueOf(pem_array[(b1 << 4 & 48) + (k >>> 4 & 15)]));
        out.append(String.valueOf(EQ));
        out.append(String.valueOf(EQ));
        return out.toString();
    }
    if (left == 2) {
        byte b2 = b[strt];
        byte b4 = b[strt + 1];
        int l = 0;
        out.append(String.valueOf(pem_array[b2 >>> 2 & 63]));
        out.append(String.valueOf(pem_array[(b2 << 4 & 48) + (b4 >>> 4 & 15)]));
        out.append(String.valueOf(pem_array[(b4 << 2 & 60) + (l >>> 6 & 3)]));
        out.append(String.valueOf(EQ));
        return out.toString();
    }
    byte b3 = b[strt];
    byte b5 = b[strt + 1];
    byte b6 = b[strt + 2];
    out.append(String.valueOf(pem_array[b3 >>> 2 & 63]));
    out.append(String.valueOf(pem_array[(b3 << 4 & 48) + (b5 >>> 4 & 15)]));
    out.append(String.valueOf(pem_array[(b5 << 2 & 60) + (b6 >>> 6 & 3)]));
    out.append(String.valueOf(pem_array[b6 & 63]));
    return out.toString();
}