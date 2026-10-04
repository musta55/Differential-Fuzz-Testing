private static long maxMemMachineOSX() {
    try {
        //in bytes
        String command = "sysctl hw.memsize";
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(command);
        String memStr = new String(pr.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return Long.parseLong(memStr.trim().substring(12, memStr.length() - 1));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}