@Override
public ArrayList<Hop> rewriteHopDAGs(ArrayList<Hop> roots, ProgramRewriteStatus state) {
    if (!OptimizerUtils.isSparkExecutionMode())
        return roots;
    if (roots == null)
        return null;
    // top-level hops never modified
    for (Hop h : roots) injectCheckpointAfterPRead(h);
    return roots;
}
// ---- helper method(s) introduced by the refactoring ----
private void injectCheckpointAfterPRead(Hop hop) {
    if (hop.isVisited())
        return;
    if (shouldInjectCheckpoint(hop)) {
        boolean isActionOnly = isActionOnly(hop, hop.getParent());
        if (!isActionOnly)
            hop.setRequiresCheckpoint(true);
    } else {
        processChildren(hop);
    }
    hop.setVisited();
}

private boolean shouldInjectCheckpoint(Hop hop) {
    boolean isMatrix = hop.getDataType().isMatrix();
    boolean isPRead = hop instanceof DataOp && ((DataOp) hop).getOp() == OpOpData.PERSISTENTREAD;
    boolean isFrameException = hop.getDataType().isFrame() && isPRead && !((DataOp) hop).getFileFormat().isIJV();
    return (isMatrix && isPRead) || (hop.requiresReblock() && !isFrameException);
}

private void processChildren(Hop hop) {
    if (hop.getInput() != null) {
        for (int i = 0; i < hop.getInput().size(); i++) injectCheckpointAfterPRead(hop.getInput().get(i));
    }
}

