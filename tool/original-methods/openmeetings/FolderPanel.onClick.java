private void onClick(AjaxRequestTarget target, BaseFileItem f) {
    String mod = getRequest().getRequestParameters().getParameterValue(PARAM_MOD).toOptionalString();
    boolean shift = false, ctrl = false;
    if (!Strings.isEmpty(mod)) {
        JSONObject o = new JSONObject(mod);
        shift = o.optBoolean(PARAM_SHIFT);
        ctrl = o.optBoolean(PARAM_CTRL);
    }
    treePanel.select(f, target, shift, ctrl);
    if (Type.FOLDER == f.getType() && treePanel.tree.getState(f) == State.COLLAPSED) {
        treePanel.tree.expand(f);
    } else {
        treePanel.update(target, f);
    }
}