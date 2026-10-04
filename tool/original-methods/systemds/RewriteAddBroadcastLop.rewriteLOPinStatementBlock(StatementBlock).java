@Override
public List<StatementBlock> rewriteLOPinStatementBlock(StatementBlock sb) {
    if (!ConfigurationManager.isBroadcastEnabled())
        return List.of(sb);
    ArrayList<Lop> lops = OperatorOrderingUtils.getLopList(sb);
    if (lops == null)
        return List.of(sb);
    ArrayList<Lop> nodesWithBroadcast = new ArrayList<>();
    for (Lop l : lops) {
        nodesWithBroadcast.add(l);
        if (isBroadcastNeeded(l)) {
            List<Lop> oldOuts = new ArrayList<>(l.getOutputs());
            // Construct a Broadcast lop that takes this Spark node as an input
            UnaryCP bc = new UnaryCP(l, Types.OpOp1.BROADCAST, l.getDataType(), l.getValueType(), Types.ExecType.CP);
            bc.setAsynchronous(true);
            //FIXME: Wire Broadcast only with the necessary outputs
            for (Lop outCP : oldOuts) {
                // Rewire l -> outCP to l -> Broadcast -> outCP
                bc.addOutput(outCP);
                outCP.replaceInput(l, bc);
                l.removeOutput(outCP);
                //FIXME: Rewire _inputParams when needed (e.g. GroupedAggregate)
            }
            //Place it immediately after the Spark lop in the node list
            nodesWithBroadcast.add(bc);
        }
    }
    // New node is added inplace in the Lop DAG
    return Arrays.asList(sb);
}