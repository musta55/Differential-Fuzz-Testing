@Override
protected void onBind() {
    super.onBind();
    getComponent().setDefaultModelObject(getText(delay));
    getComponent().setOutputMarkupId(true);
    onTimer(delay);
}