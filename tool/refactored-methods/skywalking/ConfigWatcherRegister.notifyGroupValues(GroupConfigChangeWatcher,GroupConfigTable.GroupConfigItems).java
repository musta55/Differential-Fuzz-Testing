protected void notifyGroupValues(final GroupConfigChangeWatcher watcher, final GroupConfigTable.GroupConfigItems groupConfigItems) {
    Map<String, ConfigTable.ConfigItem> groupItems = groupConfigItems.getItems();
    Map<String, ConfigChangeWatcher.ConfigChangeEvent> changedGroupItems = new HashMap<>();
    Map<String, String> currentGroupItems = Optional.ofNullable(watcher.groupItems()).orElse(new HashMap<>());
    groupItems.forEach((groupItemName, groupItem) -> {
        String newItemValue = groupItem.getValue();
        if (isGroupItemDeleted(newItemValue, currentGroupItems, groupItemName)) {
            changedGroupItems.put(groupItemName, new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
        } else if (isGroupItemChanged(newItemValue, currentGroupItems, groupItemName)) {
            changedGroupItems.put(groupItemName, new ConfigChangeWatcher.ConfigChangeEvent(newItemValue, ConfigChangeWatcher.EventType.MODIFY));
        }
    });
    currentGroupItems.forEach((oldGroupItemName, oldGroupItemValue) -> {
        if (isGroupItemRemoved(groupItems, oldGroupItemName)) {
            changedGroupItems.put(oldGroupItemName, new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
        }
    });
    if (!changedGroupItems.isEmpty()) {
        watcher.notifyGroup(changedGroupItems);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isValueDeleted(String newItemValue, ConfigChangeWatcher watcher) {
    return newItemValue == null && watcher.value() != null;
}

private boolean isValueChanged(String newItemValue, ConfigChangeWatcher watcher) {
    return newItemValue != null && !newItemValue.equals(watcher.value());
}

private boolean isGroupItemDeleted(String newItemValue, Map<String, String> currentGroupItems, String groupItemName) {
    return newItemValue == null && currentGroupItems.get(groupItemName) != null;
}

private boolean isGroupItemChanged(String newItemValue, Map<String, String> currentGroupItems, String groupItemName) {
    return newItemValue != null && !newItemValue.equals(currentGroupItems.get(groupItemName));
}

private boolean isGroupItemRemoved(Map<String, ConfigTable.ConfigItem> groupItems, String oldGroupItemName) {
    return groupItems.get(oldGroupItemName) == null;
}

