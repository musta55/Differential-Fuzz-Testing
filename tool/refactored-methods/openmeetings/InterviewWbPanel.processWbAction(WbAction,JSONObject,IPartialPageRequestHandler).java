@Override
public void processWbAction(WbAction a, JSONObject obj, IPartialPageRequestHandler handler) throws IOException {
    Client c = rp.getClient();
    switch(a) {
        case START_RECORDING:
            startRecording(c);
            break;
        case STOP_RECORDING:
            stopRecording(c);
            break;
        default:
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void startRecording(Client c) throws IOException {
    streamProcessor.startRecording(c);
}

private void stopRecording(Client c) throws IOException {
    streamProcessor.stopRecording(c);
}

