public PlanPartition(HashSet<Long> P, HashSet<Long> R, HashSet<Long> I, HashSet<Long> Pnpc, ArrayList<Long> M, InterestingPoint[] Mext, boolean hasOuter) {
    this._nodes = new HashSet<>(P);
    this._roots = new HashSet<>(R);
    this._inputs = new HashSet<>(I);
    this._nodesNpc = new HashSet<>(Pnpc);
    this._matPoints = new ArrayList<>(M);
    this._matPointsExt = Mext;
    this._hasOuter = hasOuter;
}