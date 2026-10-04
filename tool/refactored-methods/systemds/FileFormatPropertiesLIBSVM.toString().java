@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(" delim ").append(delim);
    sb.append(" indexDelim ").append(indexDelim);
    sb.append(" sparse ").append(sparse);
    return sb.toString();
}