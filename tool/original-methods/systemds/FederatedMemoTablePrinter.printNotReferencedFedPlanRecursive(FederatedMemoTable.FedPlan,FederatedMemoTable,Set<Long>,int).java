/**
 * Helper method to recursively print the FedPlan tree.
 *
 * @param plan  The current FedPlan to print
 * @param visited Set to keep track of visited FedPlans (prevents cycles)
 * @param depth   The current depth level for indentation
 */
private static void printNotReferencedFedPlanRecursive(FederatedMemoTable.FedPlan plan, FederatedMemoTable memoTable, Set<Long> visited, int depth) {
    long hopID = plan.getHopRef().getHopID();
    if (visited.contains(hopID)) {
        return;
    }
    visited.add(hopID);
    printFedPlan(plan, depth, true);
    // Process child nodes
    List<Pair<Long, FEDInstruction.FederatedOutput>> childFedPlanPairs = plan.getChildFedPlans();
    for (int i = 0; i < childFedPlanPairs.size(); i++) {
        Pair<Long, FEDInstruction.FederatedOutput> childFedPlanPair = childFedPlanPairs.get(i);
        FederatedMemoTable.FedPlanVariants childVariants = memoTable.getFedPlanVariants(childFedPlanPair);
        if (childVariants == null || childVariants.isEmpty())
            continue;
        for (FederatedMemoTable.FedPlan childPlan : childVariants.getFedPlanVariants()) {
            printNotReferencedFedPlanRecursive(childPlan, memoTable, visited, depth + 1);
        }
    }
}