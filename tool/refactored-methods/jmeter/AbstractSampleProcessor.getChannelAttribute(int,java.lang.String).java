/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.csv.processor.SampleProcessor#getChannelAttribute
     * (int, java.lang.String)
     */
@Override
public Object getChannelAttribute(int channel, String key) {
    return retrieveOrCreateChannelContext(channel).get(key);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Get the ChannelContext associated to the specified channel. If the
 * specified channel does not have a context associated to it then one will
 * be created and associated.
 *
 * @param channel
 *            The channel number whose context is to be returned
 * @return The channel context associated to the specified channel.
 */
private ChannelContext retrieveOrCreateChannelContext(int channel) {
    ensureChannelContextExists(channel);
    return channelContexts.get(channel);
}

private void ensureChannelContextExists(int channel) {
    while (channelContexts.size() <= channel) {
        channelContexts.add(new ChannelContext());
    }
}

