@Override
public int compareTo(FederatedRange o) {
    for (int i = 0; i < _beginDims.length; i++) {
        if (_beginDims[i] < o._beginDims[i])
            return -1;
        if (_beginDims[i] > o._beginDims[i])
            return 1;
        if (_endDims[i] < o._endDims[i])
            return -1;
        if (_endDims[i] > o._endDims[i])
            return 1;
    }
    return 0;
}