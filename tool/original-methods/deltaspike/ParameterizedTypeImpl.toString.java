@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(rawType);
    if (actualTypeArguments.length > 0) {
        sb.append("<");
        for (Type actualType : actualTypeArguments) {
            sb.append(actualType);
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(">");
    }
    return sb.toString();
}