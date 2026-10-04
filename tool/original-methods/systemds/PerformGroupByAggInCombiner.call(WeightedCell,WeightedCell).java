@Override
public WeightedCell call(WeightedCell value1, WeightedCell value2) throws Exception {
    WeightedCell outCell = new WeightedCell();
    CmCovObject cmObj = new CmCovObject();
    if (//everything except sum
    _op instanceof CMOperator) {
        if (((CMOperator) _op).isPartialAggregateOperator()) {
            cmObj.reset();
            // cmFn.get(key.getTag());
            CM lcmFn = CM.getCMFnObject(((CMOperator) _op).aggOpType);
            //partial aggregate cm operator
            lcmFn.execute(cmObj, value1.getValue(), value1.getWeight());
            lcmFn.execute(cmObj, value2.getValue(), value2.getWeight());
            outCell.setValue(cmObj.getRequiredPartialResult(_op));
            outCell.setWeight(cmObj.getWeight());
        } else {
            //forward tuples to reducer
            throw new DMLRuntimeException("Incorrect usage, should have used PerformGroupByAggInReducer");
        }
    } else if (//sum
    _op instanceof AggregateOperator) {
        AggregateOperator aggop = (AggregateOperator) _op;
        if (aggop.existsCorrection()) {
            KahanObject buffer = new KahanObject(aggop.initialValue, 0);
            KahanPlus.getKahanPlusFnObject();
            //partial aggregate with correction
            aggop.increOp.fn.execute(buffer, value1.getValue() * value1.getWeight());
            aggop.increOp.fn.execute(buffer, value2.getValue() * value2.getWeight());
            outCell.setValue(buffer._sum);
            outCell.setWeight(1);
        } else {
            //no correction
            double v = aggop.initialValue;
            v = aggop.increOp.fn.execute(v, value1.getValue() * value1.getWeight());
            v = aggop.increOp.fn.execute(v, value2.getValue() * value2.getWeight());
            outCell.setValue(v);
            outCell.setWeight(1);
        }
    } else
        throw new DMLRuntimeException("Unsupported operator in grouped aggregate instruction:" + _op);
    return outCell;
}