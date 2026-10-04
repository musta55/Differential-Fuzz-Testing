BaseCommand(String command, String serialNumber) {
    this.command = command;
    this.serialNumber = serialNumber;
    this.commandBuilder = Command.newBuilder();
    KeyStringValuePair.Builder arguments = KeyStringValuePair.newBuilder();
    arguments.setKey("SerialNumber");
    arguments.setValue(serialNumber);
    this.commandBuilder.setCommand(command);
    this.commandBuilder.addArgs(arguments);
}