public boolean isWidgetAdminDeleted() {
    return widgetDeletedStatus.getOrDefault(WIDGET_ID_ADMIN, false);
}