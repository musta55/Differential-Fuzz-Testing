@Override
public String toString() {
    return _op.getHopID() + " " + _op.toString() + " CompressedOutput: " + isCompressedOutput() + " IsDecompressing: " + isDecompressing();
}