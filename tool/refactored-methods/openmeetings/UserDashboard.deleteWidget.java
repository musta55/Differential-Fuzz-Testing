@Override
public void deleteWidget(String widgetId) {
    widgetDeletedStatus.put(widgetId, true);
    super.deleteWidget(widgetId);
}