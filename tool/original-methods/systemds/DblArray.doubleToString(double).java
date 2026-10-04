private static String doubleToString(double v) {
    if (v == (long) v)
        return Long.toString(((long) v));
    else
        return Double.toString(v);
}