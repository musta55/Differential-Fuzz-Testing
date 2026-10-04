public static void checkMemorySetting() {
    long JRE_Mem_Byte = Runtime.getRuntime().maxMemory();
    long Sys_Mem_Byte = maxMemMachine();
    // Default 500MB
    final long DefaultJava_500MB = 1024L * 1024 * 500;
    // 10 GB
    final long Logging_Limit = 1024L * 1024 * 1024 * 10;
    if (JRE_Mem_Byte <= DefaultJava_500MB) {
        String st = byteMemoryToString(JRE_Mem_Byte);
        LOG.warn("Low memory budget set of: " + st + " this should most likely be increased");
    } else if (JRE_Mem_Byte < Logging_Limit && JRE_Mem_Byte * 10 < Sys_Mem_Byte) {
        String st = byteMemoryToString(JRE_Mem_Byte);
        String sm = byteMemoryToString(Sys_Mem_Byte);
        LOG.warn("Low memory budget of total: " + sm + " set to: " + st);
    }
}