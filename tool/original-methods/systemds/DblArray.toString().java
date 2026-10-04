@Override
public String toString() {
    StringBuilder sb = new StringBuilder(2 + _arr.length * 4);
    sb.append("[");
    sb.append(doubleToString(_arr[0]));
    for (int i = 1; i < _arr.length; i++) {
        sb.append(", ");
        sb.append(doubleToString(_arr[i]));
    }
    sb.append("]");
    return sb.toString();
}