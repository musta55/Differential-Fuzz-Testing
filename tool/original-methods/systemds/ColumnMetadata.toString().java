@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    if (_ndistinct != DEFAULT_DISTINCT) {
        sb.append(":");
        sb.append(_ndistinct);
    }
    if (_mvValue != null)
        sb.append("--" + _mvValue);
    return sb.toString();
}