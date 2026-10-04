BaseCommand(String command, String serialNumber) {
    this.command = command;
    this.serialNumber = serialNumber;
    this.commandBuilder = Command.newBuilder().setCommand(command).addArgs(KeyStringValuePair.newBuilder().setKey("SerialNumber").setValue(serialNumber).build());
}