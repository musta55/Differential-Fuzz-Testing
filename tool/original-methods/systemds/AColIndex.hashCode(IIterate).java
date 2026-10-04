private static int hashCode(IIterate it) {
    int res = 1;
    while (it.hasNext()) res = 31 * res + it.next();
    return res;
}