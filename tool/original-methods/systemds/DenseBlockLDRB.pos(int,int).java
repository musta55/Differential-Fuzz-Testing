@Override
public int pos(int r, int c) {
    return (r % blockSize()) * _odims[0] + c;
}