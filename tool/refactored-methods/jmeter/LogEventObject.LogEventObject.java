public LogEventObject(Object source, String serializedString) {
    super(source);
    if (source instanceof LogEvent) {
        logLevel = ((LogEvent) source).getLevel();
    }
    this.serializedString = serializedString;
}