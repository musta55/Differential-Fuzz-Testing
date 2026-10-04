public void toBuffer(H5BufferBuilder bb) {
    byte classAndVersion = 17;
    bb.writeByte(classAndVersion);
    byte[] classBytes = { 32, 63, 0 };
    bb.writeBytes(classBytes);
    bb.writeInt(8);
    //bitOffset
    bb.writeShort((short) 0);
    //bitPrecision
    bb.writeShort((short) 64);
    //exponentLocation
    bb.writeByte(52);
    //exponentSize
    bb.writeByte(11);
    //mantissaLocation
    bb.writeByte(0);
    //mantissaSize
    bb.writeByte(52);
    //exponentBias
    bb.writeInt(1023);
    // reserved
    bb.writeInt(0);
}