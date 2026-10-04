@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof FileKey)) {
        return false;
    }
    FileKey that = (FileKey) obj;
    return Objects.equals(this.filename, that.filename) && Objects.equals(this.encoding, that.encoding);
}