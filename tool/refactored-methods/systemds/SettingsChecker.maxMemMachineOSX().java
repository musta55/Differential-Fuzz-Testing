private static long maxMemMachineOSX() {
    try {
        //in bytes
        String command = OS_COMMANDS.get("Mac OS");
        Process pr = new ProcessBuilder("sh", "-c", command).start();
        String memStr = new String(pr.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return Long.parseLong(memStr.trim().substring(12, memStr.length() - 1));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}