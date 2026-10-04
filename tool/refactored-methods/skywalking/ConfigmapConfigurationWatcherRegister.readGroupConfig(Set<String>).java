@Override
public Optional<GroupConfigTable> readGroupConfig(final Set<String> keys) {
    GroupConfigTable groupConfigTable = new GroupConfigTable();
    Map<String, String> configMapData = informer.configMapData();
    keys.forEach(key -> {
        GroupConfigTable.GroupConfigItems groupConfigItems = new GroupConfigTable.GroupConfigItems(key);
        groupConfigTable.addGroupConfigItems(groupConfigItems);
        addGroupConfigItems(groupConfigItems, configMapData, key);
    });
    return Optional.of(groupConfigTable);
}
// ---- helper method(s) introduced by the refactoring ----
private void addGroupConfigItems(GroupConfigTable.GroupConfigItems groupConfigItems, Map<String, String> configMapData, String key) {
    configMapData.forEach((groupItemKey, itemValue) -> {
        if (groupItemKey.startsWith(key + ".")) {
            String itemName = groupItemKey.substring(key.length() + 1);
            groupConfigItems.add(new ConfigTable.ConfigItem(itemName, itemValue));
        }
    });
}

