public static void resetVisitStatus(List<CNode> outputs) {
    for (CNode output : outputs) output.resetVisitStatus();
}