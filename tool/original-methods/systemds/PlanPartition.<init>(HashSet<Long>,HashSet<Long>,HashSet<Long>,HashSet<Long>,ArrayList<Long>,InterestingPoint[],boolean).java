public PlanPartition(HashSet<Long> P, HashSet<Long> R, HashSet<Long> I, HashSet<Long> Pnpc, ArrayList<Long> M, InterestingPoint[] Mext, boolean hasOuter) {
    _nodes = P;
    _roots = R;
    _inputs = I;
    _nodesNpc = Pnpc;
    _matPoints = M;
    _matPointsExt = Mext;
    _hasOuter = hasOuter;
}