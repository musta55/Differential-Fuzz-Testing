@Override
public String getString(int colIndexes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < _values.length; i++) {
        sb.append(_values[i]);
        if (i != _values.length - 1) {
            sb.append((i + 1) % colIndexes == 0 ? "\n" : ", ");
        }
    }
    return sb.toString();
}