@Override
public String toString() {
    StringBuilder sb = new StringBuilder("Function Dictionary:");
    sb.append("----------------------------------------\n");
    int pos = 0;
    for (Entry<String, T> e : _funs.entrySet()) {
        sb.append("-- [");
        sb.append(pos++);
        sb.append("]: ");
        sb.append(e.getKey());
        sb.append("\n");
    }
    return sb.toString();
}