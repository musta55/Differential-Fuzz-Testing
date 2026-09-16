public LogEventObject(Object source, String seralizedString) {
    super(source);
    if (source instanceof LogEvent) {
        level = ((LogEvent) source).getLevel();
    }
    this.seralizedString = seralizedString;
}