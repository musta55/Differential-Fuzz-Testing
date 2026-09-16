@Override
public void show(IPartialPageRequestHandler handler) {
    BaseFileItem last = roomFiles.getLastSelected();
    if (last.getId() == null) {
        lastSelectedRoom.add(AttributeModifier.replace(ATTR_VALUE, last.getRoomId()));
        lastSelectedOwner.add(AttributeModifier.replace(ATTR_VALUE, last.getOwnerId()));
        lastSelectedGroup.add(AttributeModifier.replace(ATTR_VALUE, last.getGroupId()));
    } else {
        lastSelectedId.add(AttributeModifier.replace(ATTR_VALUE, last.getId()));
    }
    super.show(handler);
}