public void addArgument(String name, String value) {
    Arguments myArgs = this.getArguments();
    myArgs.addArgument(new HTTPArgument(name, value));
}