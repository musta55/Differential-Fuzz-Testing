@Override
public String codegen(boolean sparse, GeneratorAPI api) {
    // note: ignore sparse flag, generate both
    String tmp = TEMPLATE;
    //generate dense/sparse bodies
    StringBuilder sb = new StringBuilder();
    for (CNode out : _outputs) sb.append(out.codegen(false, api));
    for (CNode out : _outputs) out.resetGenerated();
    //append output assignments
    for (int i = 0; i < _outputs.size(); i++) {
        CNode out = _outputs.get(i);
        String tmpOut = getAggTemplate(i);
        //get variable name (w/ handling of direct consumption of inputs)
        String varName = (out instanceof CNodeData && ((CNodeData) out).getHopID() == ((CNodeData) _inputs.get(0)).getHopID()) ? "a" : out.getVarname();
        tmpOut = tmpOut.replace("%IN%", varName);
        tmpOut = tmpOut.replace("%IX%", String.valueOf(i));
        sb.append(tmpOut);
    }
    //replace class name and body
    tmp = tmp.replace("%TMP%", createVarname());
    tmp = tmp.replace("%BODY_dense%", sb.toString());
    //replace meta data information
    String aggList = "";
    for (AggOp aggOp : _aggOps) {
        aggList += !aggList.isEmpty() ? "," : "";
        aggList += "AggOp." + aggOp.name();
    }
    tmp = tmp.replace("%AGG_OP%", aggList);
    tmp = tmp.replace("%SPARSE_SAFE%", String.valueOf(isSparseSafe()));
    return tmp;
}