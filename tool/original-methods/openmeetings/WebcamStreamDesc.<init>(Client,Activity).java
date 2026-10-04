public WebcamStreamDesc(final Client client, Activity toggle) {
    super(client, StreamType.WEBCAM);
    setWidth(client.getWidth());
    setHeight(client.getHeight());
    // we will add all allowed activities here
    if (client.isAllowed(Activity.AUDIO)) {
        activities.add(Activity.AUDIO);
    }
    if (client.isAllowed(Activity.VIDEO)) {
        activities.add(Activity.VIDEO);
    }
    if (has(Activity.AUDIO) && has(Activity.VIDEO)) {
        activities.add(Activity.AUDIO_VIDEO);
    }
    switch(toggle) {
        case AUDIO:
            if (has(toggle)) {
                micEnabled = true;
            }
            break;
        case VIDEO:
            if (has(toggle)) {
                camEnabled = true;
            }
            break;
        case AUDIO_VIDEO:
            if (has(toggle)) {
                micEnabled = true;
                camEnabled = true;
            }
            break;
        default:
    }
}