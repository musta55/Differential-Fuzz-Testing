private String prepCSVProperties() {
    StringBuilder sb = new StringBuilder();
    Data dataInput = (Data) getInputs().get(0);
    Lop headerLop = dataInput.getNamedInputLop(DataExpression.DELIM_HAS_HEADER_ROW, String.valueOf(DataExpression.DEFAULT_DELIM_HAS_HEADER_ROW));
    Lop delimLop = dataInput.getNamedInputLop(DataExpression.DELIM_DELIMITER, DataExpression.DEFAULT_DELIM_DELIMITER);
    Lop fillLop = dataInput.getNamedInputLop(DataExpression.DELIM_FILL, String.valueOf(DataExpression.DEFAULT_DELIM_FILL));
    Lop fillValueLop = dataInput.getNamedInputLop(DataExpression.DELIM_FILL_VALUE, String.valueOf(DataExpression.DEFAULT_DELIM_FILL_VALUE));
    Lop naStrings = dataInput.getNamedInputLop(DataExpression.DELIM_NA_STRINGS, String.valueOf(DataExpression.DEFAULT_NA_STRINGS));
    validateLiteral(headerLop, DataExpression.DELIM_HAS_HEADER_ROW);
    validateLiteral(delimLop, DataExpression.DELIM_DELIMITER);
    validateLiteral(fillLop, DataExpression.DELIM_FILL);
    validateLiteral(fillValueLop, DataExpression.DELIM_FILL_VALUE);
    sb.append(((Data) headerLop).getBooleanValue());
    sb.append(OPERAND_DELIMITOR);
    sb.append(((Data) delimLop).getStringValue());
    sb.append(OPERAND_DELIMITOR);
    sb.append(((Data) fillLop).getBooleanValue());
    sb.append(OPERAND_DELIMITOR);
    sb.append(((Data) fillValueLop).getDoubleValue());
    sb.append(OPERAND_DELIMITOR);
    appendNAStrings(sb, naStrings);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void validateLiteral(Lop lop, String parameterName) {
    if (lop.isVariable()) {
        throw new LopsException(this.printErrorLocation() + "Parameter " + parameterName + " must be a literal.");
    }
}

private void appendNAStrings(StringBuilder sb, Lop naStrings) {
    if (naStrings instanceof Nary) {
        Nary naLops = (Nary) naStrings;
        for (Lop na : naLops.getInputs()) {
            sb.append(((Data) na).getStringValue());
            sb.append(DataExpression.DELIM_NA_STRING_SEP);
        }
    } else if (naStrings instanceof Data) {
        sb.append(((Data) naStrings).getStringValue());
    }
}

