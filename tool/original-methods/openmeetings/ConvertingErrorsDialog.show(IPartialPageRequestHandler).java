@Override
public Modal<BaseFileItem> show(IPartialPageRequestHandler handler) {
    BaseFileItem f = getModelObject();
    headerLabel.setDefaultModel(new ResourceModel(f.getType() == BaseFileItem.Type.RECORDING ? "887" : "convert.errors.file"));
    List<FileItemLog> logs = fileLogDao.get(f);
    if (f.getHash() == null) {
        message.setVisible(true);
        message.setDefaultModelObject(getString("888"));
    } else if (!f.exists()) {
        message.setVisible(true);
        message.setDefaultModelObject(getString(f.getType() == BaseFileItem.Type.RECORDING ? "1595" : "convert.errors.file.missing"));
    } else {
        message.setVisible(false);
    }
    if (!logs.isEmpty()) {
        logView.setVisible(false);
        logView.setList(logs).setVisible(true);
    }
    handler.add(container, headerLabel);
    return super.show(handler);
}