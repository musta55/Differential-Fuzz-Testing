@Override
protected void addToString(StringBuilder sb) {
    sb.append("\nValues:").append(Arrays.stream(_values).map(Arrays::toString).reduce("", (a, b) -> a + "\n" + b));
}