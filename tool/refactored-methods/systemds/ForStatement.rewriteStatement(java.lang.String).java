@Override
public Statement rewriteStatement(String prefix) {
    throw new UnsupportedOperationException(this.printErrorLocation() + "should not call rewriteStatement for ForStatement");
}