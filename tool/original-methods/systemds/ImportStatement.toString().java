@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Statement.SOURCE + "(");
    sb.append(this._filePath + ")");
    if (this._namespace != null) {
        sb.append(" AS " + this._namespace);
    }
    sb.append(";");
    return sb.toString();
}