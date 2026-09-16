@Override
public void addWidget(Widget widget) {
    switch(widget.getId()) {
        case WIDGET_ID_RSS:
            widgetRssDeleted = false;
            break;
        case WIDGET_ID_MY_ROOMS:
            widgetMyRoomsDeleted = false;
            break;
        case WIDGET_ID_ADMIN:
            widgetAdminDeleted = false;
            break;
        default:
            break;
    }
    super.addWidget(widget);
}