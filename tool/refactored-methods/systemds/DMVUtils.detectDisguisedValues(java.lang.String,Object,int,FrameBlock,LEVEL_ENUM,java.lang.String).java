private static void detectDisguisedValues(String dom_pattern, Object col, int col_idx, FrameBlock frameBlock, LEVEL_ENUM level, String disguisedVal) {
    int row_idx = -1;
    String pattern = "";
    String[] attr = (String[]) col;
    int numRows = frameBlock.getNumRows();
    LevelStrategy strategy = levelStrategies.get(level);
    if (strategy == null) {
        throw new DMLRuntimeException("Could not find suitable level");
    }
    for (int i = 0; i < numRows; i++) {
        String value = (attr[i] == null) ? "NULL" : attr[i];
        pattern = strategy.apply(value);
        row_idx++;
        if (pattern.equals(dom_pattern))
            continue;
        frameBlock.set(row_idx, col_idx, disguisedVal);
    }
}