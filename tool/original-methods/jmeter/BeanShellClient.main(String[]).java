public static void main(String[] args) throws Exception {
    if (args.length < MINARGS) {
        System.out.println("Please provide " + MINARGS + " or more arguments:");
        System.out.println("serverhost serverport filename [arg1 arg2 ...]");
        System.out.println("e.g. ");
        System.out.println("localhost 9000 extras/remote.bsh apple blake 7");
        return;
    }
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
        // Prompt is unnecessary
        sendLine("bsh.prompt=\"\";", os);
        sendLine("String [] args={", os);
        for (int i = MINARGS; i < args.length; i++) {
            sendLine("\"" + args[i] + "\",\n", os);
        }
        sendLine("};", os);
        int b;
        try (BufferedReader fis = Files.newBufferedReader(Paths.get(file))) {
            while ((b = fis.read()) != -1) {
                os.write(b);
            }
        }
        // Reset for other users
        sendLine("bsh.prompt=\"bsh % \";", os);
        os.flush();
        // Tell server that we are done
        sock.shutdownOutput();
        // wait for script to finish
        sockRead.join();
    }
}