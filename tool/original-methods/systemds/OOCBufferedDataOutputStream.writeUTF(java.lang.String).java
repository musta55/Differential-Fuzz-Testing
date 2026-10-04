@Override
public void writeUTF(String s) throws IOException {
    int slen = s.length();
    int utflen = IOUtilFunctions.getUTFSize(s) - 2;
    if (utflen - 2 > 65535)
        throw new UTFDataFormatException("encoded string too long: " + utflen);
    writeShort(utflen);
    for (int i = 0; i < slen; i++) {
        if (_count + 3 > _bufflen)
            flushBuffer();
        final char c = s.charAt(i);
        if (c >= 0x0001 && c <= 0x007F) {
            _buff[_count++] = (byte) c;
            _position++;
        } else if (c >= 0x0800) {
            _buff[_count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
            _buff[_count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
            _buff[_count++] = (byte) (0x80 | (c & 0x3F));
            _position += 3;
        } else {
            _buff[_count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
            _buff[_count++] = (byte) (0x80 | (c & 0x3F));
            _position += 2;
        }
    }
}