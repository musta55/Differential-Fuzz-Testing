@Override
public void processInstruction(ExecutionContext ec) {
    SparkExecutionContext sec = (SparkExecutionContext) ec;
    // Get input RDDs
    JavaPairRDD<Long, FrameBlock> in1 = sec.getFrameBinaryBlockRDDHandleForVariable(input1.getName());
    String expression = sec.getScalarInput(input2).getStringValue();
    long margin = ec.getScalarInput(input3).getLongValue();
    // Create local compiled functions (once) and execute on RDD
    JavaPairRDD<Long, FrameBlock> out = in1.mapValues(new RDDStringProcessing(expression, margin));
    if (expression.contains("jaccardSim")) {
        long rows = sec.getDataCharacteristics(output.getName()).getRows();
        sec.getDataCharacteristics(output.getName()).setDimension(rows, rows);
    } else {
        long rows = sec.getDataCharacteristics(output.getName()).getRows();
        long cols = sec.getDataCharacteristics(output.getName()).getCols();
        sec.getDataCharacteristics(output.getName()).setDimension(rows, cols);
    }
    sec.setRDDHandleForVariable(output.getName(), out);
    sec.addLineageRDD(output.getName(), input1.getName());
}