@Override
public WeightedCell call(Iterable<WeightedCell> kv) throws Exception {
    WeightedCell outCell = new WeightedCell();
    CmCovObject cmObj = new CmCovObject();
    if (//everything except sum
    op instanceof CMOperator) {
        cmObj.reset();
        // cmFn.get(key.getTag());
        CM lcmFn = CM.getCMFnObject(((CMOperator) op).aggOpType);
        if (((CMOperator) op).isPartialAggregateOperator()) {
            throw new DMLRuntimeException("Incorrect usage, should have used PerformGroupByAggInCombiner");
        } else //forward tuples to reducer
        {
            for (WeightedCell value : kv) lcmFn.execute(cmObj, value.getValue(), value.getWeight());
            outCell.setValue(cmObj.getRequiredResult(op));
            outCell.setWeight(1);
        }
    } else if (//sum
    op instanceof AggregateOperator) {
        AggregateOperator aggop = (AggregateOperator) op;
        if (aggop.existsCorrection()) {
            KahanObject buffer = new KahanObject(aggop.initialValue, 0);
            KahanPlus.getKahanPlusFnObject();
            //partial aggregate with correction
            for (WeightedCell value : kv) aggop.increOp.fn.execute(buffer, value.getValue() * value.getWeight());
            outCell.setValue(buffer._sum);
            outCell.setWeight(1);
        } else {
            //no correction
            double v = aggop.initialValue;
            for (WeightedCell value : kv) v = aggop.increOp.fn.execute(v, value.getValue() * value.getWeight());
            outCell.setValue(v);
            outCell.setWeight(1);
        }
    } else
        throw new DMLRuntimeException("Unsupported operator in grouped aggregate instruction:" + op);
    return outCell;
}