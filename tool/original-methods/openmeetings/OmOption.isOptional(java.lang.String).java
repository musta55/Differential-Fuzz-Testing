public boolean isOptional(String group) {
    boolean result = false;
    if (optional != null) {
        String[] grps = group.split(",");
        for (String g : grps) {
            result |= optional.getOrDefault(g, false);
        }
    }
    return result;
}