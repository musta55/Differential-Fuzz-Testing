public String getOutputString() {
    if (c1 == null)
        return "''";
    if (c2 == null)
        return c1.toString();
    return c1.toString() + c2.toString();
}