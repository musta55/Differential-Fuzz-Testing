/**
 * @param payload
 */
@Override
public void put(T payload) {
    if ((payload instanceof Tuple) || canSendToOutput(payload)) {
        count++;
        output.put(payload);
    }
}