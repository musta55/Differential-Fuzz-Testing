@Override
public String codegen(boolean sparse, GeneratorAPI api) {
    String template = TEMPLATE;
    StringBuilder bodyBuilder = new StringBuilder();
    for (CNode out : _outputs) {
        bodyBuilder.append(out.codegen(false, api));
        out.resetGenerated();
    }
    for (int i = 0; i < _outputs.size(); i++) {
        CNode out = _outputs.get(i);
        String aggTemplate = getAggTemplate(i);
        String varName = (out instanceof CNodeData && ((CNodeData) out).getHopID() == ((CNodeData) _inputs.get(0)).getHopID()) ? "a" : out.getVarname();
        bodyBuilder.append(aggTemplate.replace("%IN%", varName).replace("%IX%", String.valueOf(i)));
    }
    template = template.replace("%TMP%", createVarname()).replace("%BODY_dense%", bodyBuilder.toString()).replace("%AGG_OP%", String.join(",", _aggOps.stream().map(AggOp::name).toArray(String[]::new))).replace("%SPARSE_SAFE%", Boolean.toString(isSparseSafe()));
    return template;
}