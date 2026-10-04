public void hoistFunctionCallsFromExpressions() {
    try {
        //handle statement blocks of all functions
        for (FunctionStatementBlock fsb : getFunctionStatementBlocks()) StatementBlock.rHoistFunctionCallsFromExpressions(fsb, this);
        //handle statement blocks of main program
        ArrayList<StatementBlock> tmp = new ArrayList<>();
        for (StatementBlock sb : _blocks) tmp.addAll(StatementBlock.rHoistFunctionCallsFromExpressions(sb, this));
        _blocks = tmp;
    } catch (LanguageException ex) {
        throw new RuntimeException(ex);
    }
}