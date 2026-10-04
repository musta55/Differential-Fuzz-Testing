@Override
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof FileKey)) {
        return false;
    }
    FileKey that = (FileKey) obj;
    return Objects.equals(this.filename, that.filename);
}