public static void main(String[] args) throws Exception {
    parseArguments(args);
    String host = args[0];
    String portString = args[1];
    String file = args[2];
    // convert to telnet port
    int port = Integer.parseInt(portString) + 1;
    System.out.println("Connecting to BSH server on " + host + ":" + portString);
    try (Socket sock = new Socket(host, port);
        InputStream is = sock.getInputStream();
        OutputStream os = sock.getOutputStream()) {
        SockRead sockRead = new SockRead(is);
        sockRead.start();
        setupScriptExecution(os, args, file);
        // Tell server that we are done
        sock.shutdownOutput();
        // wait for script to finish
        sockRead.join();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void parseArguments(String[] args) {
    if (args.length < MINARGS) {
        System.out.println("Please provide " + MINARGS + " or more arguments:");
        System.out.println("serverhost serverport filename [arg1 arg2 ...]");
        System.out.println("e.g. ");
        System.out.println("localhost 9000 extras/remote.bsh apple blake 7");
        System.exit(1);
    }
}

private static void setupScriptExecution(OutputStream os, String[] args, String file) throws IOException {
    // Prompt is unnecessary
    sendLine("bsh.prompt=\"\";", os);
    sendLine("String [] args={", os);
    for (int i = MINARGS; i < args.length; i++) {
        sendLine("\"" + args[i] + "\",\n", os);
    }
    sendLine("};", os);
    try (BufferedReader fis = Files.newBufferedReader(Paths.get(file))) {
        int b;
        while ((b = fis.read()) != -1) {
            os.write(b);
        }
    }
    // Reset for other users
    sendLine("bsh.prompt=\"bsh % \";", os);
    os.flush();
}

