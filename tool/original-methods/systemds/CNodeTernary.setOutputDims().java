@Override
public void setOutputDims() {
    switch(_type) {
        case PLUS_MULT:
        case MINUS_MULT:
        case BIASADD:
        case BIASMULT:
        case REPLACE:
        case REPLACE_NAN:
        case IFELSE:
        case LOOKUP_RC1:
            _rows = 0;
            _cols = 0;
            _dataType = DataType.SCALAR;
            break;
        case LOOKUP_RVECT1:
            _rows = 1;
            _cols = _inputs.get(0)._cols;
            _dataType = DataType.MATRIX;
            break;
    }
}