@Override
public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
    Object o = getDefaultModelObject();
    String s = getDefaultModelObjectAsString();
    if (o == null) {
        // no-op
    } else if (o instanceof Date || o.getClass().isAssignableFrom(Date.class)) {
        s = fmt.format((Date) o);
    } else if (o instanceof Calendar || o.getClass().isAssignableFrom(Calendar.class)) {
        s = fmt.format((Calendar) o);
    }
    replaceComponentTagBody(markupStream, openTag, s);
}