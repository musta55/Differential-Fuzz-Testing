/**
 * Helper method to recursively print the FedPlan tree.
 *
 * @param plan  The current FedPlan to print
 * @param visited Set to keep track of visited FedPlans (prevents cycles)
 * @param depth   The current depth level for indentation
 */
private static void printFedPlanTreeRecursive(FederatedMemoTable.FedPlan plan, FederatedMemoTable memoTable, Set<Long> visited, int depth) {
    long hopID = 0;
    if (depth == 0) {
        hopID = -1;
    } else {
        hopID = plan.getHopRef().getHopID();
    }
    if (visited.contains(hopID)) {
        return;
    }
    visited.add(hopID);
    printFedPlan(plan, depth, false);
    // Process child nodes
    List<Pair<Long, FEDInstruction.FederatedOutput>> childFedPlanPairs = plan.getChildFedPlans();
    for (int i = 0; i < childFedPlanPairs.size(); i++) {
        Pair<Long, FEDInstruction.FederatedOutput> childFedPlanPair = childFedPlanPairs.get(i);
        FederatedMemoTable.FedPlanVariants childVariants = memoTable.getFedPlanVariants(childFedPlanPair);
        if (childVariants == null || childVariants.isEmpty())
            continue;
        for (FederatedMemoTable.FedPlan childPlan : childVariants.getFedPlanVariants()) {
            printFedPlanTreeRecursive(childPlan, memoTable, visited, depth + 1);
        }
    }
}