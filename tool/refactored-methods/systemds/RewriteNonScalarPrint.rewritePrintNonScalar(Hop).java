private void rewritePrintNonScalar(Hop hop) {
    // Check if hop is a unary PRINT op
    if (HopRewriteUtils.isUnary(hop, Types.OpOp1.PRINT)) {
        // Check if child is non-scalar
        Hop child = hop.getInput().get(0);
        if (!child.getDataType().isScalar()) {
            Hop toStringOp = createToStringOp(child);
            replaceChildWithToString(hop, child, toStringOp);
            LOG.debug("Applied non-scalar print rewrite on hop ID = " + hop.getHopID());
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Hop createToStringOp(Hop child) {
    LinkedHashMap<String, Hop> args = new LinkedHashMap<>();
    args.put("target", child);
    return HopRewriteUtils.createParameterizedBuiltinOp(child, args, Types.ParamBuiltinOp.TOSTRING);
}

private void replaceChildWithToString(Hop hop, Hop child, Hop toStringOp) {
    HopRewriteUtils.replaceChildReference(hop, child, toStringOp, 0);
}

