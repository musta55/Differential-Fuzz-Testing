@Override
protected void onTimer(AjaxRequestTarget target) {
    int remain = remain(System.currentTimeMillis());
    if (remain > -1) {
        getComponent().setDefaultModelObject(getText(remain));
        onTimer(remain);
        target.add(getComponent());
    } else {
        stop(target);
        onFinish(target);
    }
}