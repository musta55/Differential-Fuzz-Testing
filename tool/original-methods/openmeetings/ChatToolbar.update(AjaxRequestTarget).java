void update(AjaxRequestTarget target) {
    final boolean admin = hasAdminLevel(getRights());
    chatForm.process(() -> {
        target.add(save.setVisible(admin), delBtn.setVisible(admin));
        return true;
    }, r -> {
        final boolean moder = admin || isModerator(cm, getUserId(), r.getId());
        target.add(save.setVisible(moder), delBtn.setVisible(moder));
        return true;
    }, u -> {
        target.add(save.setVisible(true), delBtn.setVisible(true));
        return true;
    });
}