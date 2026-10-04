protected void notifyGroupValues(final GroupConfigChangeWatcher watcher, final GroupConfigTable.GroupConfigItems groupConfigItems) {
    Map<String, ConfigTable.ConfigItem> groupItems = groupConfigItems.getItems();
    Map<String, ConfigChangeWatcher.ConfigChangeEvent> changedGroupItems = new HashMap<>();
    Map<String, String> currentGroupItems = Optional.ofNullable(watcher.groupItems()).orElse(new HashMap<>());
    groupItems.forEach((groupItemName, groupItem) -> {
        String newItemValue = groupItem.getValue();
        if (newItemValue == null) {
            if (currentGroupItems.get(groupItemName) != null) {
                // Notify watcher, the new value is null with delete event type.
                changedGroupItems.put(groupItemName, new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
            } else {
                // Don't need to notify, stay in null.
            }
        } else {
            //add and modify
            if (!newItemValue.equals(currentGroupItems.get(groupItemName))) {
                changedGroupItems.put(groupItemName, new ConfigChangeWatcher.ConfigChangeEvent(newItemValue, ConfigChangeWatcher.EventType.MODIFY));
            } else {
                // Don't need to notify, stay in the same config value.
            }
        }
    });
    currentGroupItems.forEach((oldGroupItemName, oldGroupItemValue) -> {
        //delete item
        if (null == groupItems.get(oldGroupItemName)) {
            // Notify watcher, the item is deleted with delete event type.
            changedGroupItems.put(oldGroupItemName, new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
        }
    });
    if (changedGroupItems.size() > 0) {
        watcher.notifyGroup(changedGroupItems);
    }
}