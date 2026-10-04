public static long maxMemMachine() {
    String sys = System.getProperty("os.name");
    if ("Linux".equals(sys)) {
        return maxMemMachineLinux() * 1024;
    } else if (sys.contains("Mac OS")) {
        return maxMemMachineOSX();
    } else if (sys.startsWith("Windows")) {
        return maxMemMachineWin();
    } else {
        return -1;
    }
}