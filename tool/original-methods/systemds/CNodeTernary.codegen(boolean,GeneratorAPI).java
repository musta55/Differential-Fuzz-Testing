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
    //		String tmp = _type.getTemplate(lsparse, api, lang);
    String tmp = getLanguageTemplateClass(this, api).getTemplate(_type, lsparse);
    tmp = tmp.replace("%TMP%", var);
    for (int j = 1; j <= 3; j++) {
        String varj = _inputs.get(j - 1).getVarname();
        //replace sparse and dense inputs
        tmp = tmp.replace("%IN" + j + "v%", varj + (varj.startsWith("a") ? "vals" : varj.startsWith("STMP") ? ".values()" : ""));
        tmp = tmp.replace("%IN" + j + "i%", varj + (varj.startsWith("a") ? "ix" : varj.startsWith("STMP") ? ".indexes()" : ""));
        tmp = tmp.replace("%IN" + j + "%", varj);
        tmp = tmp.replace("%POS%", varj.startsWith("a") ? varj + "i" : varj.startsWith("STMP") ? "0" : "");
        tmp = tmp.replace("%LEN%", varj.startsWith("a") ? "alen" : varj.startsWith("STMP") ? varj + ".size()" : "");
    }
    sb.append(tmp);
    //mark as generated
    _generated = true;
    return sb.toString();
}