@Override
public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
    if (!isVisible(openTag, markupStream)) {
        // skip the body
        return;
    }
    super.onComponentTagBody(markupStream, openTag);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isVisible(ComponentTag openTag, MarkupStream markupStream) {
    final String vis = openTag.getAttribute(markupStream.getWicketNamespace() + WICKET_VISIBLE);
    return vis == null || !Boolean.FALSE.equals(Boolean.valueOf(vis));
}

