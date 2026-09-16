public boolean isOptional(String group) {
    if (optional == null) {
        return false;
    }
    return Stream.of(group.split(",")).anyMatch(g -> optional.getOrDefault(g, false));
}