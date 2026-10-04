@Override
public void writeUTF(String s) throws IOException {
    int slen = s.length();
    int utflen = IOUtilFunctions.getUTFSize(s) - 2;
    if (utflen - 2 > 65535)
        throw new UTFDataFormatException("encoded string too long: " + utflen);
    writeShort(utflen);
    for (int i = 0; i < slen; i++) {
        char c = s.charAt(i);
        if (c >= 0x0001 && c <= 0x007F) {
            writeByte(c);
        } else if (c >= 0x0800) {
            writeByte(0xE0 | ((c >> 12) & 0x0F));
            writeByte(0x80 | ((c >> 6) & 0x3F));
            writeByte(0x80 | (c & 0x3F));
        } else {
            writeByte(0xC0 | ((c >> 6) & 0x1F));
            writeByte(0x80 | (c & 0x3F));
        }
    }
}