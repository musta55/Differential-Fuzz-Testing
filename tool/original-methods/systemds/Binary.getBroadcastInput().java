@Override
public Lop getBroadcastInput() {
    if (getExecType() != ExecType.SPARK)
        return null;
    ArrayList<Lop> inputs = getInputs();
    if (inputs.get(0).getDataType() == DataType.FRAME && inputs.get(1).getDataType() == DataType.MATRIX)
        return inputs.get(1);
    else
        return null;
}