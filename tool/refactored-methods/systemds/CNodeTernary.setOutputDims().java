@Override
public void setOutputDims() {
    switch(_type) {
        case PLUS_MULT, MINUS_MULT, BIASADD, BIASMULT, REPLACE, REPLACE_NAN, IFELSE, LOOKUP_RC1 ->
            {
                _rows = 0;
                _cols = 0;
                _dataType = DataType.SCALAR;
            }
        case LOOKUP_RVECT1 ->
            {
                _rows = 1;
                _cols = _inputs.get(0)._cols;
                _dataType = DataType.MATRIX;
            }
    }
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

