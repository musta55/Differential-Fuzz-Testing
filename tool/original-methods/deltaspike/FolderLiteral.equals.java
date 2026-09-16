/*
    * generated
    */
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
        return false;
    }
    if (!(o instanceof FolderLiteral)) {
        return false;
    }
    if (!super.equals(o)) {
        return false;
    }
    FolderLiteral that = (FolderLiteral) o;
    if (!folderNameBuilder.equals(that.folderNameBuilder)) {
        return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
        return false;
    }
    return true;
}