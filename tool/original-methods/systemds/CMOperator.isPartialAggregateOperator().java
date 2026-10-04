public boolean isPartialAggregateOperator() {
    boolean ret = false;
    switch(aggOpType) {
        case COUNT:
        case MEAN:
            ret = true;
            break;
        //NOTE: the following aggregation operators are not marked for partial aggregation
        //because they required multiple intermediate values and hence do not apply to the
        //grouped aggregate combiner which needs to work on value/weight pairs only.
        case CM2:
        case CM3:
        case CM4:
        case VARIANCE:
            ret = false;
            break;
        default:
    }
    return ret;
}