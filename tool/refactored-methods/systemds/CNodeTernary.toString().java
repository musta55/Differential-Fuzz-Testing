@Override
public String toString() {
    return switch(_type) {
        case PLUS_MULT ->
            "t(+*)";
        case MINUS_MULT ->
            "t(-*)";
        case BIASADD ->
            "t(bias+)";
        case BIASMULT ->
            "t(bias*)";
        case REPLACE, REPLACE_NAN ->
            "t(rplc)";
        case IFELSE ->
            "t(ifelse)";
        case LOOKUP_RC1 ->
            "u(ixrc1)";
        case LOOKUP_RVECT1 ->
            "u(ixrv1)";
        default ->
            super.toString();
    };
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

