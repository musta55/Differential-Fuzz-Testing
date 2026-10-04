protected static void parseLineageTraceDedup(String str) {
    str.replaceAll("\r\n", "\n");
    String[] allPatches = str.split("\n\n");
    for (String patch : allPatches) {
        String[] headBody = patch.split("\r\n|\r|\n", 2);
        // Parse the header (e.g. patch_R_SB15_1)
        String[] parts = headBody[0].split(LineageDedupUtils.DEDUP_DELIM);
        // Deserialize the patch
        LineageItem patchLi = parseLineageTrace(headBody[1]);
        Long pathId = Long.parseLong(parts[3]);
        // Map the pathID and the DAG root name to the deserialized DAG.
        String loopName = parts[2];
        if (!LineageRecomputeUtils.loopPatchMap.containsKey(loopName))
            LineageRecomputeUtils.loopPatchMap.put(loopName, new DedupLoopItem(loopName));
        DedupLoopItem loopItem = LineageRecomputeUtils.loopPatchMap.get(loopName);
        if (!loopItem.patchLiMap.containsKey(pathId)) {
            loopItem.patchLiMap.put(pathId, new HashMap<>());
        }
        loopItem.patchLiMap.get(pathId).put(parts[1], patchLi);
    }
}