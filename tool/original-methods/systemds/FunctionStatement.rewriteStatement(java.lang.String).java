@Override
public Statement rewriteStatement(String prefix) {
    throw new LanguageException(this.printErrorLocation() + "should not call rewriteStatement for FunctionStatement");
}