@Override
public void validateExpression(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    if (getOutput() instanceof DataIdentifier) {
        // set properties for Data identifier
        String name = ((DataIdentifier) getOutput()).getName();
        Identifier id = ids.get(name);
        if (id == null) {
            //undefined variables are always treated unconditionally as error in order to prevent common script-level bugs
            raiseValidateError("Undefined Variable (" + name + ") used in statement", false, LanguageErrorCodes.INVALID_PARAMETERS);
        }
        getOutput().setProperties(id);
        // validate IndexedIdentifier -- which is substype of DataIdentifer with index
        if (getOutput() instanceof IndexedIdentifier) {
            // validate the row / col index bounds (if defined)
            IndexedIdentifier ixId = (IndexedIdentifier) getOutput();
            Expression[] exp = new Expression[] { ixId.getRowLowerBound(), ixId.getRowUpperBound(), ixId.getColLowerBound(), ixId.getColUpperBound() };
            String[] msg = new String[] { "row lower", "row upper", "column lower", "column upper" };
            for (int i = 0; i < 4; i++) {
                if (exp[i] != null) {
                    exp[i].validateExpression(ids, constVars, conditional);
                    if (exp[i].getOutput().getDataType() == DataType.MATRIX) {
                        raiseValidateError("Matrix values for " + msg[i] + " index bound are " + "not supported, which includes indexed identifiers.", conditional);
                    }
                }
            }
            if (getOutput().getDataType() == DataType.LIST) {
                int dim1 = (((IndexedIdentifier) getOutput()).getRowUpperBound() == null) ? 1 : -1;
                ((IndexedIdentifier) getOutput()).setDimensions(dim1, 1);
            } else {
                //default
                IndexPair updatedIndices = ((IndexedIdentifier) getOutput()).calculateIndexedDimensions(ids, constVars, conditional);
                ((IndexedIdentifier) getOutput()).setDimensions(updatedIndices._row, updatedIndices._col);
            }
        }
    } else {
        this.getOutput().setProperties(this.getOutput());
    }
}