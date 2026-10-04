public static long maxMemMachine() {
    String sys = System.getProperty("os.name");
    if (OS_COMMANDS.containsKey("Linux") && sys.equals("Linux")) {
        return maxMemMachineLinux() * 1024;
    } else if (OS_COMMANDS.containsKey("Mac OS") && sys.contains("Mac OS")) {
        return maxMemMachineOSX();
    } else if (OS_COMMANDS.containsKey("Windows") && sys.startsWith("Windows")) {
        return maxMemMachineWin();
    } else {
        return -1;
    }
}