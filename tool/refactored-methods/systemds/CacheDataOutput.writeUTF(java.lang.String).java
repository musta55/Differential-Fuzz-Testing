@Override
public void writeUTF(String s) throws UTFDataFormatException {
    int slen = s.length();
    int utflen = IOUtilFunctions.getUTFSize(s) - 2;
    if (utflen > 65535)
        throw new UTFDataFormatException("encoded string too long: " + utflen);
    //write utf len (2 bytes)
    writeShort(utflen);
    //write utf payload
    for (int i = 0; i < slen; i++) {
        char c = s.charAt(i);
        if (//1 byte range
        c >= 0x0001 && c <= 0x007F)
            writeByte(c);
        else if (c >= 0x0800) {
            //3 byte range
            _buff[_count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
            _buff[_count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
            _buff[_count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
        } else {
            //2 byte range and null
            _buff[_count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
            _buff[_count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
        }
    }
}