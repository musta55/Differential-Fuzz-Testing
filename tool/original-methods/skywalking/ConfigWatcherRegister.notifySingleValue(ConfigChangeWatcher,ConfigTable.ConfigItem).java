protected void notifySingleValue(final ConfigChangeWatcher watcher, ConfigTable.ConfigItem configItem) {
    String newItemValue = configItem.getValue();
    if (newItemValue == null) {
        if (watcher.value() != null) {
            // Notify watcher, the new value is null with delete event type.
            watcher.notify(new ConfigChangeWatcher.ConfigChangeEvent(null, ConfigChangeWatcher.EventType.DELETE));
        } else {
            // Don't need to notify, stay in null.
        }
    } else {
        if (!newItemValue.equals(watcher.value())) {
            watcher.notify(new ConfigChangeWatcher.ConfigChangeEvent(newItemValue, ConfigChangeWatcher.EventType.MODIFY));
        } else {
            // Don't need to notify, stay in the same config value.
        }
    }
}