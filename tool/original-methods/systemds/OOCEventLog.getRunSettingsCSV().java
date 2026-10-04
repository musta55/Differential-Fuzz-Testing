public static String getRunSettingsCSV() {
    StringBuilder sb = new StringBuilder();
    Set<Map.Entry<String, Object>> entrySet = _runSettings.entrySet();
    int ctr = 0;
    for (Map.Entry<String, Object> entry : entrySet) {
        sb.append(entry.getKey());
        ctr++;
        if (ctr >= entrySet.size())
            sb.append('\n');
        else
            sb.append(',');
    }
    ctr = 0;
    for (Map.Entry<String, Object> entry : _runSettings.entrySet()) {
        sb.append(entry.getValue());
        ctr++;
        if (ctr < entrySet.size())
            sb.append(',');
    }
    return sb.toString();
}