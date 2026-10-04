protected static void checkOutputDataType(CPOperand in1, CPOperand in2, CPOperand out) {
    // check for valid data type of output
    if ((in1.getDataType() == DataType.MATRIX || in2.getDataType() == DataType.MATRIX) && out.getDataType() != DataType.MATRIX)
        throw new DMLRuntimeException("Element-wise matrix operations between variables " + in1.getName() + " and " + in2.getName() + " must produce a matrix, which " + out.getName() + " is not");
}