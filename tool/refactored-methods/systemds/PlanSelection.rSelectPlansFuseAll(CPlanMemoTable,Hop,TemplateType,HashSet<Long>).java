protected void rSelectPlansFuseAll(CPlanMemoTable memo, Hop current, TemplateType currentType, HashSet<Long> partition) {
    if (isVisited(current.getHopID(), currentType) || (partition != null && !partition.contains(current.getHopID())))
        return;
    pruneSubsumedPlans(memo, current);
    selectPlanForCurrentPath(memo, current, currentType);
    processChildren(memo, current, currentType, partition);
    setVisited(current.getHopID(), currentType);
}
// ---- helper method(s) introduced by the refactoring ----
private void pruneSubsumedPlans(CPlanMemoTable memo, Hop current) {
    if (memo.contains(current.getHopID())) {
        HashSet<MemoTableEntry> rmSet = new HashSet<>();
        List<MemoTableEntry> hopP = memo.get(current.getHopID());
        for (MemoTableEntry e1 : hopP) for (MemoTableEntry e2 : hopP) if (e1 != e2 && e1.subsumes(e2))
            rmSet.add(e2);
        memo.remove(current, rmSet);
    }
}

private void selectPlanForCurrentPath(CPlanMemoTable memo, Hop current, TemplateType currentType) {
    MemoTableEntry best = null;
    if (memo.contains(current.getHopID())) {
        if (currentType == null) {
            best = memo.get(current.getHopID()).stream().filter(p -> p.isValid()).min(BASE_COMPARE).orElse(null);
        } else {
            _typedCompare.setType(currentType);
            best = memo.get(current.getHopID()).stream().filter(p -> p.type == currentType || p.type == TemplateType.CELL).min(_typedCompare).orElse(null);
        }
        addBestPlan(current.getHopID(), best);
    }
}

private void processChildren(CPlanMemoTable memo, Hop current, TemplateType currentType, HashSet<Long> partition) {
    for (int i = 0; i < current.getInput().size(); i++) {
        TemplateType pref = (bestPlan(current.getHopID()) != null && bestPlan(current.getHopID()).isPlanRef(i)) ? bestPlan(current.getHopID()).type : null;
        rSelectPlansFuseAll(memo, current.getInput().get(i), pref, partition);
    }
}

private MemoTableEntry bestPlan(long hopID) {
    List<MemoTableEntry> plans = _bestPlans.get(hopID);
    return plans != null ? plans.get(plans.size() - 1) : null;
}

