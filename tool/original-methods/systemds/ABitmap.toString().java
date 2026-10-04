@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("  zeros:  " + _numZeros);
    sb.append("\nOffsets:" + Arrays.toString(_offsetsLists));
    addToString(sb);
    return sb.toString();
}