private static void printFedPlan(FederatedMemoTable.FedPlan plan, int depth, boolean isNotReferenced) {
    StringBuilder sb = new StringBuilder();
    Hop hop = plan.getHopRef();
    if (depth == 0) {
        sb.append("(R) ROOT [Root]").append(String.format(" {Total: %.1f}", plan.getCumulativeCost()));
    } else {
        sb.append(String.format("(%d) ", hop.getHopID())).append(hop.getOpString()).append(" [").append(isNotReferenced ? "NRef" : plan.getFedOutType()).append("]").append(formatChildNodes(plan)).append(String.format(" {Total: %.1f, Self: %.1f, Net: %.1f, Weight: %.1f}", plan.getCumulativeCost(), plan.getSelfCost(), plan.getForwardingCost(), plan.getComputeWeight())).append(formatMatrixCharacteristics(hop)).append(formatMemoryEstimates(hop)).append(formatReblockAndCheckpointRequirements(hop)).append(formatExecutionType(hop));
    }
    System.out.println(sb);
}
// ---- helper method(s) introduced by the refactoring ----
private static long getHopId(FederatedMemoTable.FedPlan plan, int depth) {
    return depth == 0 ? -1 : plan.getHopRef().getHopID();
}

private static void processChildNodes(FederatedMemoTable.FedPlan plan, FederatedMemoTable memoTable, Set<Long> visited, int depth) {
    for (Pair<Long, FederatedOutput> childPair : plan.getChildFedPlans()) {
        FederatedMemoTable.FedPlanVariants childVariants = memoTable.getFedPlanVariants(childPair);
        if (childVariants == null || childVariants.isEmpty()) {
            continue;
        }
        for (FederatedMemoTable.FedPlan childPlan : childVariants.getFedPlanVariants()) {
            printFedPlanTreeRecursive(childPlan, memoTable, visited, depth + 1);
        }
    }
}

private static String formatChildNodes(FederatedMemoTable.FedPlan plan) {
    StringBuilder childs = new StringBuilder(" (");
    boolean childAdded = false;
    for (Pair<Long, FederatedOutput> childPair : plan.getChildFedPlans()) {
        childs.append(childAdded ? "," : "").append(childPair.getLeft());
        childAdded = true;
    }
    return childAdded ? childs.append(")").toString() : "";
}

private static String formatMatrixCharacteristics(Hop hop) {
    return String.format(" [%d, %d, %d, %d%s]", hop.getDim1(), hop.getDim2(), hop.getBlocksize(), hop.getNnz(), hop.getUpdateType().isInPlace() ? ", " + hop.getUpdateType().toString().toLowerCase() : "");
}

private static String formatMemoryEstimates(Hop hop) {
    return String.format(" [%d, %d, %d -> %dMB]", OptimizerUtils.toMB(hop.getInputMemEstimate()), OptimizerUtils.toMB(hop.getIntermediateMemEstimate()), OptimizerUtils.toMB(hop.getOutputMemEstimate()), OptimizerUtils.toMB(hop.getMemEstimate()));
}

private static String formatReblockAndCheckpointRequirements(Hop hop) {
    if (hop.requiresReblock() && hop.requiresCheckpoint()) {
        return " [rblk, chkpt]";
    } else if (hop.requiresReblock()) {
        return " [rblk]";
    } else if (hop.requiresCheckpoint()) {
        return " [chkpt]";
    }
    return "";
}

private static String formatExecutionType(Hop hop) {
    return hop.getExecType() != null ? ", " + hop.getExecType() : "";
}

