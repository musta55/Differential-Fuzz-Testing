private void move(AjaxRequestTarget target, BaseFileItem p, BaseFileItem f) {
    Long pid = p.getId();
    if (pid != null && pid.equals(f.getId())) {
        return;
    }
    f.setParentId(pid);
    f.setOwnerId(p.getOwnerId());
    f.setRoomId(p.getRoomId());
    f.setGroupId(p.getGroupId());
    if (f instanceof Recording rec) {
        recDao.update(rec);
    } else {
        fileDao.update((FileItem) f);
    }
    treePanel.updateNode(target, f);
}