@Override
public WeightedCell call(Iterable<WeightedCell> kv) throws Exception {
    WeightedCell outCell = new WeightedCell();
    if (op instanceof CMOperator) {
        handleCMOperator(kv, outCell);
    } else if (op instanceof AggregateOperator) {
        handleAggregateOperator(kv, outCell);
    } else {
        throw new DMLRuntimeException("Unsupported operator in grouped aggregate instruction:" + op);
    }
    return outCell;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleCMOperator(Iterable<WeightedCell> kv, WeightedCell outCell) {
    CmCovObject cmObj = new CmCovObject();
    cmObj.reset();
    CM lcmFn = CM.getCMFnObject(((CMOperator) op).aggOpType);
    if (((CMOperator) op).isPartialAggregateOperator()) {
        throw new DMLRuntimeException("Incorrect usage, should have used PerformGroupByAggInCombiner");
    } else {
        for (WeightedCell value : kv) lcmFn.execute(cmObj, value.getValue(), value.getWeight());
        outCell.setValue(cmObj.getRequiredResult(op));
        outCell.setWeight(1);
    }
}

private void handleAggregateOperator(Iterable<WeightedCell> kv, WeightedCell outCell) {
    AggregateOperator aggop = (AggregateOperator) op;
    if (aggop.existsCorrection()) {
        handleAggregateWithCorrection(kv, outCell, aggop);
    } else {
        handleAggregateWithoutCorrection(kv, outCell, aggop);
    }
}

private void handleAggregateWithCorrection(Iterable<WeightedCell> kv, WeightedCell outCell, AggregateOperator aggop) {
    KahanObject buffer = new KahanObject(aggop.initialValue, 0);
    for (WeightedCell value : kv) aggop.increOp.fn.execute(buffer, value.getValue() * value.getWeight());
    outCell.setValue(buffer._sum);
    outCell.setWeight(1);
}

private void handleAggregateWithoutCorrection(Iterable<WeightedCell> kv, WeightedCell outCell, AggregateOperator aggop) {
    double v = aggop.initialValue;
    for (WeightedCell value : kv) v = aggop.increOp.fn.execute(v, value.getValue() * value.getWeight());
    outCell.setValue(v);
    outCell.setWeight(1);
}

