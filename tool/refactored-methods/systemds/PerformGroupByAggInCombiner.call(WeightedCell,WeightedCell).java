@Override
public WeightedCell call(WeightedCell value1, WeightedCell value2) throws Exception {
    WeightedCell outCell = new WeightedCell();
    if (_op instanceof CMOperator) {
        handleCMOperator(value1, value2, outCell);
    } else if (_op instanceof AggregateOperator) {
        handleAggregateOperator(value1, value2, outCell);
    } else {
        throw new DMLRuntimeException("Unsupported operator in grouped aggregate instruction:" + _op);
    }
    return outCell;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleCMOperator(WeightedCell value1, WeightedCell value2, WeightedCell outCell) {
    CMOperator cmOp = (CMOperator) _op;
    if (cmOp.isPartialAggregateOperator()) {
        CmCovObject cmObj = new CmCovObject();
        cmObj.reset();
        CM lcmFn = CM.getCMFnObject(cmOp.aggOpType);
        lcmFn.execute(cmObj, value1.getValue(), value1.getWeight());
        lcmFn.execute(cmObj, value2.getValue(), value2.getWeight());
        outCell.setValue(cmObj.getRequiredPartialResult(_op));
        outCell.setWeight(cmObj.getWeight());
    } else {
        throw new DMLRuntimeException("Incorrect usage, should have used PerformGroupByAggInReducer");
    }
}

private void handleAggregateOperator(WeightedCell value1, WeightedCell value2, WeightedCell outCell) {
    AggregateOperator aggOp = (AggregateOperator) _op;
    if (aggOp.existsCorrection()) {
        KahanObject buffer = new KahanObject(aggOp.initialValue, 0);
        KahanPlus kplus = KahanPlus.getKahanPlusFnObject();
        kplus.execute(buffer, value1.getValue() * value1.getWeight());
        kplus.execute(buffer, value2.getValue() * value2.getWeight());
        outCell.setValue(buffer._sum);
        outCell.setWeight(1);
    } else {
        double v = aggOp.initialValue;
        v = aggOp.increOp.fn.execute(v, value1.getValue() * value1.getWeight());
        v = aggOp.increOp.fn.execute(v, value2.getValue() * value2.getWeight());
        outCell.setValue(v);
        outCell.setWeight(1);
    }
}

