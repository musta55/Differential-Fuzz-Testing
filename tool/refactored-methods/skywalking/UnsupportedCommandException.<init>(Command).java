public UnsupportedCommandException(final Command command) {
    super("Unsupported command: " + command);
    this.command = command;
}