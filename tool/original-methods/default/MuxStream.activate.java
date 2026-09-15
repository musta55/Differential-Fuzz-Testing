/**
 * @param context
 */
@Override
public void activate(StreamContext context) {
    @SuppressWarnings("unchecked")
    Sink<Object>[] newSinks = (Sink<Object>[]) Array.newInstance(Sink.class, outputs.size());
    int i = 0;
    for (final Sink<Object> s : outputs.values()) {
        newSinks[i++] = s;
    }
    sinks = newSinks;
}