@Override
public void processInstruction(ExecutionContext ec) {
    ScalarObject so1 = ec.getScalarInput(input1);
    ScalarObject so2 = ec.getScalarInput(input2);
    String opcode = getOpcode();
    BinaryOperator dop = (BinaryOperator) _optr;
    ScalarObject sores = null;
    if (dop.fn instanceof ValueComparisonFunction) {
        sores = handleComparisonOperation(so1, so2, (ValueComparisonFunction) dop.fn);
    } else {
        sores = handleArithmeticOperation(so1, so2, dop, opcode);
    }
    ec.setScalarOutput(output.getName(), sores);
}
// ---- helper method(s) introduced by the refactoring ----
private ScalarObject handleComparisonOperation(ScalarObject so1, ScalarObject so2, ValueComparisonFunction vcomp) {
    if (so1 instanceof StringObject || so2 instanceof StringObject)
        return new BooleanObject(vcomp.compare(so1.getStringValue(), so2.getStringValue()));
    else if (so1 instanceof DoubleObject || so2 instanceof DoubleObject)
        return new BooleanObject(vcomp.compare(so1.getDoubleValue(), so2.getDoubleValue()));
    else if (so1 instanceof IntObject || so2 instanceof IntObject)
        return new BooleanObject(vcomp.compare(so1.getLongValue(), so2.getLongValue()));
    else
        //all boolean
        return new BooleanObject(vcomp.compare(so1.getBooleanValue(), so2.getBooleanValue()));
}

private ScalarObject handleArithmeticOperation(ScalarObject so1, ScalarObject so2, BinaryOperator dop, String opcode) {
    if (so1 instanceof StringObject || so2 instanceof StringObject) {
        if (//not string concatenation
        !opcode.equals(Opcodes.PLUS.toString()))
            throw new DMLRuntimeException("Arithmetic '" + opcode + "' not supported over string inputs.");
        return new StringObject(dop.fn.execute(so1.getLanguageSpecificStringValue(), so2.getLanguageSpecificStringValue()));
    } else if (so1 instanceof DoubleObject || so2 instanceof DoubleObject || output.getValueType() == ValueType.FP64) {
        return new DoubleObject(dop.fn.execute(so1.getDoubleValue(), so2.getDoubleValue()));
    } else if (so1 instanceof IntObject || so2 instanceof IntObject) {
        double tmp = dop.fn.execute(so1.getLongValue(), so2.getLongValue());
        if (//cast to long if no overflow, otherwise controlled exception
        tmp > Long.MAX_VALUE)
            throw new DMLRuntimeException("Integer operation created numerical result overflow (" + tmp + " > " + Long.MAX_VALUE + ").");
        return new IntObject((long) tmp);
    } else {
        //all boolean
        //NOTE: boolean-boolean arithmetic treated as double for consistency with R
        if (opcode.equals(Opcodes.AND.toString()) || opcode.equals(Opcodes.OR.toString()) || opcode.equals(Opcodes.XOR.toString()))
            return new BooleanObject(dop.fn.execute(so1.getBooleanValue(), so2.getBooleanValue()));
        else
            return new DoubleObject(dop.fn.execute(so1.getDoubleValue(), so2.getDoubleValue()));
    }
}

