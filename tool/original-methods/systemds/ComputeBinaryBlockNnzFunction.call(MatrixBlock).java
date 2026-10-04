@Override
public MatrixBlock call(MatrixBlock arg0) {
    _aNnz.add(arg0.getNonZeros());
    return arg0;
}