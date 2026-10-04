public WebcamStreamDesc(final Client client, Activity toggle) {
    super(client, StreamType.WEBCAM);
    setWidth(client.getWidth());
    setHeight(client.getHeight());
    addAllowedActivities(client);
    updateToggleState(toggle);
}
// ---- helper method(s) introduced by the refactoring ----
private void addAllowedActivities(Client client) {
    if (client.isAllowed(Activity.AUDIO)) {
        activities.add(Activity.AUDIO);
    }
    if (client.isAllowed(Activity.VIDEO)) {
        activities.add(Activity.VIDEO);
    }
    if (has(Activity.AUDIO) && has(Activity.VIDEO)) {
        activities.add(Activity.AUDIO_VIDEO);
    }
}

private void updateToggleState(Activity toggle) {
    if (has(toggle)) {
        switch(toggle) {
            case AUDIO:
                micEnabled = true;
                break;
            case VIDEO:
                camEnabled = true;
                break;
            case AUDIO_VIDEO:
                micEnabled = true;
                camEnabled = true;
                break;
            default:
        }
    }
}

