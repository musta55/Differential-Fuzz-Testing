private static long maxMemMachineWin() {
    try {
        //in bytes
        String command = OS_COMMANDS.get("Windows");
        Process pr = new ProcessBuilder("cmd.exe", "/c", command).start();
        String[] memStr = new String(pr.getInputStream().readAllBytes(), StandardCharsets.UTF_8).split("\n");
        //skip header, and aggregate DIMM capacities
        long capacity = 0;
        for (int i = 1; i < memStr.length; i++) {
            String tmp = memStr[i].trim();
            if (tmp.length() > 0)
                capacity += Long.parseLong(tmp);
        }
        return capacity;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}