private static void printFedPlan(FederatedMemoTable.FedPlan plan, int depth, boolean isNotReferenced) {
    StringBuilder sb = new StringBuilder();
    Hop hop = null;
    if (depth == 0) {
        sb.append("(R) ROOT [Root]");
    } else {
        hop = plan.getHopRef();
        // Add FedPlan information
        sb.append(String.format("(%d) ", hop.getHopID())).append(hop.getOpString()).append(" [");
        if (isNotReferenced) {
            sb.append("NRef");
        } else {
            sb.append(plan.getFedOutType());
        }
        sb.append("]");
    }
    StringBuilder childs = new StringBuilder();
    childs.append(" (");
    boolean childAdded = false;
    for (Pair<Long, FederatedOutput> childPair : plan.getChildFedPlans()) {
        childs.append(childAdded ? "," : "");
        childs.append(childPair.getLeft());
        childAdded = true;
    }
    childs.append(")");
    if (childAdded)
        sb.append(childs.toString());
    if (depth == 0) {
        sb.append(String.format(" {Total: %.1f}", plan.getCumulativeCost()));
        System.out.println(sb);
        return;
    }
    sb.append(String.format(" {Total: %.1f, Self: %.1f, Net: %.1f, Weight: %.1f}", plan.getCumulativeCost(), plan.getSelfCost(), plan.getForwardingCost(), plan.getComputeWeight()));
    // Add matrix characteristics
    sb.append(" [").append(hop.getDim1()).append(", ").append(hop.getDim2()).append(", ").append(hop.getBlocksize()).append(", ").append(hop.getNnz());
    if (hop.getUpdateType().isInPlace()) {
        sb.append(", ").append(hop.getUpdateType().toString().toLowerCase());
    }
    sb.append("]");
    // Add memory estimates
    sb.append(" [").append(OptimizerUtils.toMB(hop.getInputMemEstimate())).append(", ").append(OptimizerUtils.toMB(hop.getIntermediateMemEstimate())).append(", ").append(OptimizerUtils.toMB(hop.getOutputMemEstimate())).append(" -> ").append(OptimizerUtils.toMB(hop.getMemEstimate())).append("MB]");
    // Add reblock and checkpoint requirements
    if (hop.requiresReblock() && hop.requiresCheckpoint()) {
        sb.append(" [rblk, chkpt]");
    } else if (hop.requiresReblock()) {
        sb.append(" [rblk]");
    } else if (hop.requiresCheckpoint()) {
        sb.append(" [chkpt]");
    }
    // Add execution type
    if (hop.getExecType() != null) {
        sb.append(", ").append(hop.getExecType());
    }
    System.out.println(sb);
}