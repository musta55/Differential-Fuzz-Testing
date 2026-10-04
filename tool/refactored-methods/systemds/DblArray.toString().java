@Override
public String toString() {
    StringBuilder sb = new StringBuilder(2 + _arr.length * 4);
    sb.append('[');
    for (int i = 0; i < _arr.length; i++) {
        if (i > 0)
            sb.append(", ");
        sb.append(doubleToString(_arr[i]));
    }
    sb.append(']');
    return sb.toString();
}