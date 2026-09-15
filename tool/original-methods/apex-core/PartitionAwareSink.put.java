/**
 * @param payload
 */
@Override
public void put(T payload) {
    if (payload instanceof Tuple) {
        count++;
        output.put(payload);
    } else if (canSendToOutput(payload)) {
        count++;
        output.put(payload);
    }
}