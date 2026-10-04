@Override
public void processWbAction(WbAction a, JSONObject obj, IPartialPageRequestHandler handler) throws IOException {
    Client c = rp.getClient();
    switch(a) {
        case START_RECORDING:
            streamProcessor.startRecording(c);
            break;
        case STOP_RECORDING:
            streamProcessor.stopRecording(c);
            break;
        default:
    }
}