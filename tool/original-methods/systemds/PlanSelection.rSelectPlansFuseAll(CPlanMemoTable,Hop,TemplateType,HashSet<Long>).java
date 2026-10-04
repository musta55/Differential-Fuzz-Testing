protected void rSelectPlansFuseAll(CPlanMemoTable memo, Hop current, TemplateType currentType, HashSet<Long> partition) {
    if (isVisited(current.getHopID(), currentType) || (partition != null && !partition.contains(current.getHopID())))
        return;
    //step 1: prune subsumed plans of same type
    if (memo.contains(current.getHopID())) {
        HashSet<MemoTableEntry> rmSet = new HashSet<>();
        List<MemoTableEntry> hopP = memo.get(current.getHopID());
        for (MemoTableEntry e1 : hopP) for (MemoTableEntry e2 : hopP) if (e1 != e2 && e1.subsumes(e2))
            rmSet.add(e2);
        memo.remove(current, rmSet);
    }
    //step 2: select plan for current path
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
    //step 3: recursively process children
    for (int i = 0; i < current.getInput().size(); i++) {
        TemplateType pref = (best != null && best.isPlanRef(i)) ? best.type : null;
        rSelectPlansFuseAll(memo, current.getInput().get(i), pref, partition);
    }
    setVisited(current.getHopID(), currentType);
}