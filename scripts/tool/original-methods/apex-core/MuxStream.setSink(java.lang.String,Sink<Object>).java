/**
 * @param id
 * @param sink
 */
@Override
public void setSink(String id, Sink<Object> sink) {
    if (sink == null) {
        outputs.remove(id);
        if (outputs.isEmpty()) {
            sinks = NO_SINKS;
        }
    } else {
        outputs.put(id, sink);
        if (sinks != NO_SINKS) {
            activate(null);
        }
    }
}