@Override
protected void addToString(StringBuilder sb) {
    sb.append("\nValues:");
    for (double[] vv : _values) sb.append("\n" + Arrays.toString(vv));
}