@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    final int s = size();
    if (s == 0)
        return "";
    final int[] indexes = indexes();
    final double[] values = values();
    final int rowDigits = (int) Math.max(Math.ceil(Math.log10(indexes[s - 1])), 1);
    for (int i = 0; i < s; i++) {
        if (values[i] == (long) values[i])
            sb.append(String.format("%" + rowDigits + "d:%d", indexes[i], (long) values[i]));
        else
            sb.append(String.format("%" + rowDigits + "d:%s", indexes[i], Double.toString(values[i])));
        if (i + 1 < s)
            sb.append(" ");
    }
    return sb.toString();
}