@Override
public int pos(int[] ix) {
    int pos = pos(ix[0]);
    pos += ix[ix.length - 1];
    for (int i = 1; i < ix.length - 1; i++) pos += ix[i] * _odims[i];
    return pos;
}