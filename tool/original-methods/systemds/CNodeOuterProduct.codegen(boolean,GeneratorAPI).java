@Override
public String codegen(boolean sparse, GeneratorAPI api) {
    // note: ignore sparse flag, generate both
    String tmp = TEMPLATE;
    //generate dense/sparse bodies
    String tmpDense = _output.codegen(false, api);
    _output.resetGenerated();
    tmp = tmp.replace("%TMP%", createVarname());
    if (_type == OutProdType.LEFT_OUTER_PRODUCT || _type == OutProdType.RIGHT_OUTER_PRODUCT) {
        tmp = tmp.replace("%BODY_dense%", tmpDense);
        tmp = tmp.replace("%OUT%", "c");
        tmp = tmp.replace("%BODY_cellwise%", "");
        tmp = tmp.replace("%OUT_cellwise%", "0");
    } else {
        tmp = tmp.replace("%BODY_dense%", "");
        tmp = tmp.replace("%BODY_cellwise%", tmpDense);
        tmp = tmp.replace("%OUT_cellwise%", _output.getVarname());
    }
    //replace size information
    tmp = tmp.replace("%LEN%", "len");
    tmp = tmp.replace("%POSOUT%", "ci");
    tmp = tmp.replace("%TYPE%", _type.toString());
    return tmp;
}