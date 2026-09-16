@Override
public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
    final String vis = openTag.getAttribute(markupStream.getWicketNamespace() + WICKET_VISIBLE);
    if (vis != null && Boolean.FALSE.equals(Boolean.valueOf(vis))) {
        //skip the body
        return;
    }
    super.onComponentTagBody(markupStream, openTag);
}