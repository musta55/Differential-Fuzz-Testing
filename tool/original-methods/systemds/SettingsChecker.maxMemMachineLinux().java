private static long maxMemMachineLinux() {
    //in kilo bytes
    try (BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"))) {
        String currentLine = reader.readLine();
        while (!currentLine.contains("MemTotal:")) currentLine = reader.readLine();
        return Long.parseLong(currentLine.split(":")[1].split("kB")[0].strip());
    } catch (Exception e) {
        e.printStackTrace();
        return -1;
    }
}