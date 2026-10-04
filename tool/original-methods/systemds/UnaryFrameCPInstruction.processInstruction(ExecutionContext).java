@Override
public void processInstruction(ExecutionContext ec) {
    if (getOpcode().equals(Opcodes.TYPEOF.toString())) {
        FrameBlock inBlock = ec.getFrameInput(input1.getName());
        FrameBlock retBlock = inBlock.getSchemaTypeOf();
        ec.releaseFrameInput(input1.getName());
        ec.setFrameOutput(output.getName(), retBlock);
    } else if (getOpcode().equals(Opcodes.DETECTSCHEMA.toString())) {
        FrameBlock inBlock = ec.getFrameInput(input1.getName());
        FrameBlock retBlock = inBlock.detectSchema(((MultiThreadedOperator) _optr).getNumThreads());
        ec.releaseFrameInput(input1.getName());
        ec.setFrameOutput(output.getName(), retBlock);
    } else if (getOpcode().equals(Opcodes.COLNAMES.toString())) {
        FrameBlock inBlock = ec.getFrameInput(input1.getName());
        FrameBlock retBlock = inBlock.getColumnNamesAsFrame();
        ec.releaseFrameInput(input1.getName());
        ec.setFrameOutput(output.getName(), retBlock);
    } else
        throw new DMLScriptException("Opcode '" + getOpcode() + "' is not a valid UnaryFrameCPInstruction");
}