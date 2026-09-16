/*
    * generated
    */
@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (!(o instanceof FolderLiteral)) {
        return false;
    }
    FolderLiteral that = (FolderLiteral) o;
    return super.equals(o) && Objects.equals(folderNameBuilder, that.folderNameBuilder) && Objects.equals(name, that.name);
}