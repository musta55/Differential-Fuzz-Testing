public FunctionCallCP(ArrayList<Lop> inputs, String fnamespace, String fname, String[] inputNames, String[] outputNames, boolean opt, ExecType et) {
    this(inputs, fnamespace, fname, inputNames, outputNames, opt, et, 1);
}