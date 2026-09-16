public UserDashboard(String id, String title) {
    super(id, title);
    widgetDeletedStatus.put(WIDGET_ID_RSS, false);
    widgetDeletedStatus.put(WIDGET_ID_MY_ROOMS, false);
    widgetDeletedStatus.put(WIDGET_ID_ADMIN, false);
}