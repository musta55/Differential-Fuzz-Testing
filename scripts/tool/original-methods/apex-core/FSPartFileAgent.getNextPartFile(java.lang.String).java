public static String getNextPartFile(String partFile) {
    if (partFile == null) {
        return "part0.txt";
    } else if (partFile.startsWith("part") && partFile.endsWith(".txt")) {
        return "part" + (Integer.valueOf(partFile.substring(4, partFile.length() - 4)) + 1) + ".txt";
    }
    return null;
}