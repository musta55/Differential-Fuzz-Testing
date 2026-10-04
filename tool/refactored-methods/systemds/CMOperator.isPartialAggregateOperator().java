public boolean isPartialAggregateOperator() {
    switch(aggOpType) {
        case COUNT:
        case MEAN:
            return true;
        case CM2:
        case CM3:
        case CM4:
        case VARIANCE:
        default:
            return false;
    }
}