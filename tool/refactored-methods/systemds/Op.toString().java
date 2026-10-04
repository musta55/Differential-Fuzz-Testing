@Override
public String toString() {
    return hopOperation.getHopID() + " " + hopOperation.toString() + " CompressedOutput: " + isCompressedOutput() + " IsDecompressing: " + isDecompressing();
}