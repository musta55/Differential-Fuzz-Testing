@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("   " + _size);
    for (int i = 0; i < _data.length; i++) {
        DArrayIListEntry ent = _data[i];
        if (ent != null) {
            sb.append("\n");
            sb.append("[");
            ent.toString(sb);
            sb.append("]");
        }
    }
    return sb.toString();
}