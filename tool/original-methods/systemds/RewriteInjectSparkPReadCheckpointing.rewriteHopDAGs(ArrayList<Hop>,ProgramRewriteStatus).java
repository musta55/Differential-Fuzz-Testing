@Override
public ArrayList<Hop> rewriteHopDAGs(ArrayList<Hop> roots, ProgramRewriteStatus state) {
    if (!OptimizerUtils.isSparkExecutionMode())
        return roots;
    if (roots == null)
        return null;
    // top-level hops never modified
    for (Hop h : roots) rInjectCheckpointAfterPRead(h);
    return roots;
}