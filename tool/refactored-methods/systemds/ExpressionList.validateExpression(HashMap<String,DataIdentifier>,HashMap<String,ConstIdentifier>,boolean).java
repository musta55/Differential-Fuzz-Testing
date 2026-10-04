@Override
public void validateExpression(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> currConstVars, boolean conditional) {
    for (Expression ex : value) {
        ex.validateExpression(ids, currConstVars, conditional);
    }
}