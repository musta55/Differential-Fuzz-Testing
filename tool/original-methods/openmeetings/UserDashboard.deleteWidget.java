@Override
public void deleteWidget(String widgetId) {
    switch(widgetId) {
        case WIDGET_ID_RSS:
            widgetRssDeleted = true;
            break;
        case WIDGET_ID_MY_ROOMS:
            widgetMyRoomsDeleted = true;
            break;
        case WIDGET_ID_ADMIN:
            widgetAdminDeleted = true;
            break;
        default:
            break;
    }
    super.deleteWidget(widgetId);
}