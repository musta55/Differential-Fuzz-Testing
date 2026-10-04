@Override
public void setOutputDims() {
    switch(_type) {
        case VECT_CBIND:
            _rows = _inputs.get(0)._rows;
            _cols = 0;
            for (CNode in : _inputs) _cols += in._cols;
            _dataType = DataType.MATRIX;
            break;
        case VECT_MAX_POOL:
        case VECT_AVG_POOL:
            {
                //only stride 1, pad 0
                int C = Integer.parseInt(_inputs.get(6).getVarname());
                int H = Integer.parseInt(_inputs.get(7).getVarname());
                int W = Integer.parseInt(_inputs.get(8).getVarname());
                int R = Integer.parseInt(_inputs.get(11).getVarname());
                int S = Integer.parseInt(_inputs.get(12).getVarname());
                long P = DnnUtils.getP(H, R, 1, 0);
                long Q = DnnUtils.getQ(W, S, 1, 0);
                //N
                _rows = _inputs.get(0)._rows;
                _cols = C * P * Q;
                _dataType = DataType.MATRIX;
                break;
            }
        case VECT_IM2COL:
            _rows = 1;
            _cols = -1;
            _dataType = DataType.MATRIX;
            break;
        case VECT_CONV2DMM:
            {
                int H = Integer.parseInt(_inputs.get(8).getVarname());
                int W = Integer.parseInt(_inputs.get(9).getVarname());
                int K = Integer.parseInt(_inputs.get(10).getVarname());
                int R = Integer.parseInt(_inputs.get(12).getVarname());
                int S = Integer.parseInt(_inputs.get(13).getVarname());
                long P = DnnUtils.getP(H, R, 1, 0);
                long Q = DnnUtils.getQ(W, S, 1, 0);
                //N
                _rows = _inputs.get(0)._rows;
                _cols = K * P * Q;
                _dataType = DataType.MATRIX;
            }
    }
}