@Override
public String toString() {
    StringBuilder sb = new StringBuilder(getClass().getSimpleName());
    if (_ndistinct != DEFAULT_DISTINCT) {
        sb.append(":").append(_ndistinct);
    }
    if (_mvValue != null) {
        sb.append("--").append(_mvValue);
    }
    return sb.toString();
}