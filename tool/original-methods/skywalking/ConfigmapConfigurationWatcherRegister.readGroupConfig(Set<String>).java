@Override
public Optional<GroupConfigTable> readGroupConfig(final Set<String> keys) {
    GroupConfigTable groupConfigTable = new GroupConfigTable();
    Map<String, String> configMapData = informer.configMapData();
    keys.forEach(key -> {
        GroupConfigTable.GroupConfigItems groupConfigItems = new GroupConfigTable.GroupConfigItems(key);
        groupConfigTable.addGroupConfigItems(groupConfigItems);
        configMapData.forEach((groupItemKey, itemValue) -> {
            if (groupItemKey.startsWith(key + ".")) {
                String itemName = groupItemKey.substring(key.length() + 1);
                groupConfigItems.add(new ConfigTable.ConfigItem(itemName, itemValue));
            }
        });
    });
    return Optional.of(groupConfigTable);
}