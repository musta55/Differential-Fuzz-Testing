@Override
public String codegen(boolean sparse, GeneratorAPI api) {
    if (isGenerated())
        return "";
    StringBuilder sb = new StringBuilder();
    //generate children
    sb.append(_inputs.get(0).codegen(sparse, api));
    sb.append(_inputs.get(1).codegen(sparse, api));
    sb.append(_inputs.get(2).codegen(sparse, api));
    //generate binary operation
    boolean lsparse = sparse && (_inputs.get(0) instanceof CNodeData && _inputs.get(0).getVarname().startsWith("a") && !_inputs.get(0).isLiteral());
    String var = createVarname();
    String tmp = getLanguageTemplateClass(this, api).getTemplate(_type, lsparse);
    tmp = tmp.replace("%TMP%", var);
    for (int j = 1; j <= 3; j++) {
        String varj = _inputs.get(j - 1).getVarname();
        tmp = replaceInputVariables(tmp, j, varj);
    }
    sb.append(tmp);
    //mark as generated
    _generated = true;
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private String replaceInputVariables(String template, int index, String varname) {
    template = template.replace("%IN" + index + "v%", varname + (varname.startsWith("a") ? "vals" : varname.startsWith("STMP") ? ".values()" : ""));
    template = template.replace("%IN" + index + "i%", varname + (varname.startsWith("a") ? "ix" : varname.startsWith("STMP") ? ".indexes()" : ""));
    template = template.replace("%IN" + index + "%", varname);
    template = template.replace("%POS%", varname.startsWith("a") ? varname + "i" : varname.startsWith("STMP") ? "0" : "");
    template = template.replace("%LEN%", varname.startsWith("a") ? "alen" : varname.startsWith("STMP") ? varname + ".size()" : "");
    return template;
}

