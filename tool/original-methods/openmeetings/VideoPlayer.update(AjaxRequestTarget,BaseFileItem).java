public VideoPlayer update(AjaxRequestTarget target, BaseFileItem r) {
    boolean videoExists = r != null && r.exists();
    if (videoExists) {
        PageParameters pp = new PageParameters();
        if (r instanceof Recording) {
            pp.add("id", r.getId());
            mp4Rec.setPageParameters(pp);
            player.replace(mp4Rec);
            player.setPoster(posterRecRes, pp);
        } else {
            pp.add("id", r.getId()).add("uid", findParent(MainPanel.class).getClient().getUid());
            mp4File.setPageParameters(pp);
            player.replace(mp4File);
            player.setPoster(posterFileRes, new PageParameters(pp).add("preview", true));
        }
    }
    container.setVisible(videoExists);
    if (target != null) {
        target.add(container);
    }
    return this;
}