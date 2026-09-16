public List<BaseFileItem> getByParent(BaseFileItem node, Long id) {
    List<BaseFileItem> list = new ArrayList<>();
    if (node instanceof Recording rec) {
        List<Recording> recList;
        if (id == null) {
            if (node.getOwnerId() == null) {
                recList = recDao.getRootByPublic(rec.getGroupId());
            } else {
                recList = recDao.getRootByOwner(node.getOwnerId());
            }
        } else {
            recList = recDao.getByParent(id);
        }
        list.addAll(recList);
    } else {
        List<FileItem> fileList;
        if (id == null) {
            if (node.getRoomId() != null) {
                fileList = fileDao.getByRoom(node.getRoomId());
            } else if (node.getGroupId() != null) {
                fileList = fileDao.getByGroup(node.getGroupId(), roomId == null ? VIDEO_TYPES : null);
            } else {
                fileList = fileDao.getByOwner(node.getOwnerId());
            }
        } else {
            fileList = fileDao.getByParent(id, roomId == null ? VIDEO_TYPES : null);
        }
        list.addAll(fileList);
    }
    if (node.isReadOnly()) {
        for (BaseFileItem f : list) {
            f.setReadOnly(true);
        }
    }
    return list;
}