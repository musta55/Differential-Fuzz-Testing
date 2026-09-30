public static String getNextPartFile(String partFile) {
    if (partFile == null) {
        return "part0.txt";
    }
    if (!isValidPartFileFormat(partFile)) {
        return null;
    }
    return incrementPartFileNumber(partFile);
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isValidPartFileFormat(String partFile) {
    return partFile.startsWith("part") && partFile.endsWith(".txt");
}

private static String incrementPartFileNumber(String partFile) {
    int number = Integer.parseInt(partFile.substring(4, partFile.length() - 4));
    return "part" + (number + 1) + ".txt";
}

