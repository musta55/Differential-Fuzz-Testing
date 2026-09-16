public VideoPlayer(String id) {
    super(id);
    setRenderBodyOnly(true);
    add(container.setOutputMarkupPlaceholderTag(true));
    setupSources();
    player.add(mp4Rec);
    container.add(player);
    update(null, null);
}
// ---- helper method(s) introduced by the refactoring ----
private void setupSources() {
    mp4Rec.setDisplayType(true);
    mp4Rec.setType(MP4_MIME_TYPE);
    mp4File.setDisplayType(true);
    mp4File.setType(MP4_MIME_TYPE);
}

private PageParameters createPageParameters(BaseFileItem r) {
    PageParameters pp = new PageParameters();
    pp.add("id", r.getId());
    return pp;
}

private void updateRecordingSource(PageParameters pp) {
    mp4Rec.setPageParameters(pp);
    player.replace(mp4Rec);
    player.setPoster(posterRecRes, pp);
}

private void updateFileSource(PageParameters pp) {
    String uid = findParent(MainPanel.class).getClient().getUid();
    pp.add("uid", uid);
    mp4File.setPageParameters(pp);
    player.replace(mp4File);
    player.setPoster(posterFileRes, new PageParameters(pp).add("preview", true));
}

