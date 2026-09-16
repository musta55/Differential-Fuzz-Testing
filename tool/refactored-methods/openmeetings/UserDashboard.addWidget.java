@Override
public void addWidget(Widget widget) {
    widgetDeletedStatus.put(widget.getId(), false);
    super.addWidget(widget);
}