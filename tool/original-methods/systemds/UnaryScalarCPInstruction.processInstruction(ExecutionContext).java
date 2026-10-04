@Override
public void processInstruction(ExecutionContext ec) {
    String opcode = getOpcode();
    ScalarObject sores = null;
    ScalarObject so = null;
    //get the scalar input
    so = ec.getScalarInput(input1);
    //core execution
    if (opcode.equalsIgnoreCase(Opcodes.PRINT.toString())) {
        String outString = so.getLanguageSpecificStringValue();
        // print to stdout only when suppress flag in DMLScript is not set.
        // The flag will be set, for example, when SystemDS is invoked in fenced mode from Jaql.
        if (!DMLScript.suppressPrint2Stdout())
            System.out.println(outString);
        // String that is printed on stdout will be inserted into symbol table (dummy, not necessary!)
        sores = new StringObject(outString);
    } else if (opcode.equalsIgnoreCase(Opcodes.STOP.toString())) {
        String message = so.getStringValue();
        if (message != null && !message.isEmpty())
            throw new DMLScriptException(message);
        else
            throw new DMLScriptException("Stop Called");
    } else if (opcode.equalsIgnoreCase(Opcodes.ASSERT.toString())) {
        sores = new BooleanObject(so.getBooleanValue());
        if (!so.getBooleanValue()) {
            String fileName = (getFilename() == null) ? "" : getFilename() + " ";
            throw new DMLScriptException("assertion failed at " + fileName + getBeginLine() + ":" + getBeginColumn() + "-" + getEndLine() + ":" + getEndColumn());
        }
    } else {
        UnaryOperator dop = (UnaryOperator) _optr;
        if (so instanceof IntObject && output.getValueType() == ValueType.INT64)
            sores = new IntObject((long) dop.fn.execute(so.getLongValue()));
        else if (so instanceof BooleanObject && output.getValueType() == ValueType.BOOLEAN)
            sores = new BooleanObject(dop.fn.execute(so.getBooleanValue()));
        else
            sores = new DoubleObject(dop.fn.execute(so.getDoubleValue()));
    }
    ec.setScalarOutput(output.getName(), sores);
}