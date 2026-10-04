protected static String[] separateMainAndDedupPatches(String str) {
    str.replaceAll("\r\n", "\n");
    String[] allPatches = str.split("\n\n");
    if (//no dedup patches
    allPatches.length == 1)
        return allPatches;
    // Merge the dedup patches into a single string
    String[] patches = new String[2];
    patches[0] = allPatches[0];
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < allPatches.length; i++) {
        sb.append(allPatches[i]);
        sb.append("\n\n");
    }
    patches[1] = sb.toString();
    return patches;
}