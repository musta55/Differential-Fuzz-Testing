public HtmlPane() {
    this.addHyperlinkListener(e -> {
        if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
            return;
        }
        String ref = e.getURL().getRef();
        if (ref != null) {
            log.debug("reference to scroll to = '{}'", ref);
            if (ref.length() > 0) {
                scrollToReference(ref);
            } else {
                // href="#"
                scrollRectToVisible(new Rectangle(1, 1, 1, 1));
            }
        }
    });
}