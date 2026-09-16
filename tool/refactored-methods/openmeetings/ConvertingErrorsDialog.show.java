@Override
public Modal<BaseFileItem> show(IPartialPageRequestHandler handler) {
    BaseFileItem f = getModelObject();
    setHeaderLabelBasedOnFileType(f);
    List<FileItemLog> logs = fileLogDao.get(f);
    setMessageVisibilityAndText(f, logs);
    setLogViewVisibilityAndLogs(logs);
    handler.add(container, headerLabel);
    return super.show(handler);
}
// ---- helper method(s) introduced by the refactoring ----
private void addLabelsToItem(ListItem<FileItemLog> item, FileItemLog l) {
    item.add(new Label("exitCode", l.getExitCode()));
    item.add(new Label("message", l.getMessage()));
}

private void addAlertClassesToItem(ListItem<FileItemLog> item, FileItemLog l) {
    if (!l.isOk()) {
        item.add(AttributeModifier.append(ATTR_CLASS, "alert"));
    }
    if (l.isWarn()) {
        item.add(AttributeModifier.append(ATTR_CLASS, "warn"));
    }
}

private void setHeaderLabelBasedOnFileType(BaseFileItem f) {
    headerLabel.setDefaultModel(new ResourceModel(f.getType() == BaseFileItem.Type.RECORDING ? "887" : "convert.errors.file"));
}

private void setMessageVisibilityAndText(BaseFileItem f, List<FileItemLog> logs) {
    if (f.getHash() == null) {
        message.setVisible(true);
        message.setDefaultModelObject(getString("888"));
    } else if (!f.exists()) {
        message.setVisible(true);
        message.setDefaultModelObject(getString(f.getType() == BaseFileItem.Type.RECORDING ? "1595" : "convert.errors.file.missing"));
    } else {
        message.setVisible(false);
    }
}

private void setLogViewVisibilityAndLogs(List<FileItemLog> logs) {
    if (!logs.isEmpty()) {
        logView.setVisible(true);
        logView.setList(logs);
    } else {
        logView.setVisible(false);
    }
}

