/**
 */
@Override
public void teardown() {
    outputs.clear();
    sinks = NO_SINKS;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateOutputs(String id, Sink<Object> sink) {
    if (sink == null) {
        outputs.remove(id);
    } else {
        outputs.put(id, sink);
    }
}

private Sink<Object>[] createSinksArray(HashMap<String, Sink<Object>> outputs) {
    @SuppressWarnings("unchecked")
    Sink<Object>[] newSinks = (Sink<Object>[]) Array.newInstance(Sink.class, outputs.size());
    int i = 0;
    for (Sink<Object> s : outputs.values()) {
        newSinks[i++] = s;
    }
    return newSinks;
}

