@Override
protected double computeOutputMemEstimate(long dim1, long dim2, long nnz) {
    double ret = 0;
    switch(getValueType()) {
        case INT64:
            ret = OptimizerUtils.INT_SIZE;
            break;
        case FP64:
            ret = OptimizerUtils.DOUBLE_SIZE;
            break;
        case BOOLEAN:
            ret = OptimizerUtils.BOOLEAN_SIZE;
            break;
        case STRING:
            ret = this.value_string.length() * OptimizerUtils.CHAR_SIZE;
            break;
        case UNKNOWN:
            ret = OptimizerUtils.DEFAULT_SIZE;
            break;
        default:
            ret = 0;
    }
    return ret;
}