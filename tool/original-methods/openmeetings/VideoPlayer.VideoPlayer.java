public VideoPlayer(String id) {
    super(id);
    setRenderBodyOnly(true);
    add(container.setOutputMarkupPlaceholderTag(true));
    mp4Rec.setDisplayType(true);
    mp4Rec.setType(MP4_MIME_TYPE);
    mp4File.setDisplayType(true);
    mp4File.setType(MP4_MIME_TYPE);
    player.add(mp4Rec);
    container.add(player);
    update(null, null);
}