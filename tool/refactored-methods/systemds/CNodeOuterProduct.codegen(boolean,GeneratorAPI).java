@Override
public String codegen(boolean sparse, GeneratorAPI api) {
    String template = TEMPLATE;
    String denseBody = generateDenseBody(api);
    template = replaceTemplatePlaceholders(template, denseBody);
    return template;
}
// ---- helper method(s) introduced by the refactoring ----
private String generateDenseBody(GeneratorAPI api) {
    String body = _output.codegen(false, api);
    _output.resetGenerated();
    return body;
}

private String replaceTemplatePlaceholders(String template, String denseBody) {
    template = template.replace("%TMP%", createVarname());
    if (isLeftOrRightOuterProduct()) {
        template = template.replace("%BODY_dense%", denseBody);
        template = template.replace("%OUT%", "c");
        template = template.replace("%BODY_cellwise%", "");
        template = template.replace("%OUT_cellwise%", "0");
    } else {
        template = template.replace("%BODY_dense%", "");
        template = template.replace("%BODY_cellwise%", denseBody);
        template = template.replace("%OUT_cellwise%", _output.getVarname());
    }
    template = template.replace("%LEN%", "len");
    template = template.replace("%POSOUT%", "ci");
    template = template.replace("%TYPE%", _type.toString());
    return template;
}

private boolean isLeftOrRightOuterProduct() {
    return _type == OutProdType.LEFT_OUTER_PRODUCT || _type == OutProdType.RIGHT_OUTER_PRODUCT;
}

