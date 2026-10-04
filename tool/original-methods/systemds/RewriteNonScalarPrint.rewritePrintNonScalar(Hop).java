private void rewritePrintNonScalar(Hop hop) {
    // Check if hop is a unary PRINT op
    if (HopRewriteUtils.isUnary(hop, Types.OpOp1.PRINT)) {
        // Check if child is non-scalar
        Hop child = hop.getInput().get(0);
        if (!child.getDataType().isScalar()) {
            LinkedHashMap<String, Hop> args = new LinkedHashMap<>();
            args.put("target", child);
            // create toString hop
            Hop toStringOp = HopRewriteUtils.createParameterizedBuiltinOp(child, args, Types.ParamBuiltinOp.TOSTRING);
            // Replace child with toString in hop
            HopRewriteUtils.replaceChildReference(hop, child, toStringOp, 0);
            LOG.debug("Applied non-scalar print rewrite on hop ID = " + hop.getHopID());
        }
    }
}