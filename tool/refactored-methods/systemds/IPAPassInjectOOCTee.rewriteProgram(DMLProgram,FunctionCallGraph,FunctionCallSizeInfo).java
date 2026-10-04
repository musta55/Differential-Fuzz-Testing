@Override
public boolean rewriteProgram(DMLProgram prog, FunctionCallGraph fgraph, FunctionCallSizeInfo fcallSizes) {
    return handleRewrite(prog);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean handleRewrite(DMLProgram prog) {
    try {
        ProgramRewriter rewriter = new ProgramRewriter(new RewriteInjectOOCTee());
        ProgramRewriteStatus status = new ProgramRewriteStatus();
        rewriter.rewriteProgramHopDAGs(prog, true, status);
        return false;
    } catch (LanguageException ex) {
        throw new HopsException(ex);
    }
}

