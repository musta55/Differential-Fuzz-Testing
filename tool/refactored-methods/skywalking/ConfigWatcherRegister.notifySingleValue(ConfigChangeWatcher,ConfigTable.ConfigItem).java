protected void notifySingleValue(final ConfigChangeWatcher watcher, ConfigTable.ConfigItem configItem) {
    String newItemValue = configItem.getValue();
    if (isValueDeleted(newItemValue, watcher)) {
        watcher.notify(new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
    } else if (isValueChanged(newItemValue, watcher)) {
        watcher.notify(new ConfigChangeWatcher.ConfigChangeEvent(newItemValue, ConfigChangeWatcher.EventType.MODIFY));
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

