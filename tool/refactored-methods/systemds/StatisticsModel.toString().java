@Override
public String toString() {
    return String.format(JsonFormat, listToString(utilization), listToString(traffic), listToString(events), listToString(dataObjects), listToString(requests), listToString(heavyHitters));
}
// ---- helper method(s) introduced by the refactoring ----
private String listToString(List<?> list) {
    return list != null ? list.stream().map(Object::toString).collect(Collectors.joining(",")) : null;
}

