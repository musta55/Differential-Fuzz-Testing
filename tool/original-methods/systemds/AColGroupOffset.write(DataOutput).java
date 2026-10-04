@Override
public void write(DataOutput out) throws IOException {
    super.write(out);
    // write bitmaps (lens and data, offset later recreated)
    out.writeInt(_ptr.length);
    for (int i = 0; i < _ptr.length; i++) out.writeInt(_ptr[i]);
    out.writeInt(_data.length);
    for (int i = 0; i < _data.length; i++) out.writeChar(_data[i]);
    out.writeBoolean(_zeros);
}