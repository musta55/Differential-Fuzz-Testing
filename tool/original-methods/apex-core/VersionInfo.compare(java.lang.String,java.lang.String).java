/**
 * Compares two version strings.
 *
 * @param str1 a string of ordinal numbers separated by decimal points.
 * @param str2 a string of ordinal numbers separated by decimal points.
 * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive integer
 * if str1 is _numerically_ greater than str2. The result is zero if the strings are _numerically_ equal.
 */
public static int compare(String str1, String str2) {
    String[] vals1 = normalizeVersion(str1).split("\\.");
    String[] vals2 = normalizeVersion(str2).split("\\.");
    int i = 0;
    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
        i++;
    }
    if (i < vals1.length && i < vals2.length) {
        if (vals1[i].isEmpty()) {
            vals1[i] = "0";
        }
        if (vals2[i].isEmpty()) {
            vals2[i] = "0";
        }
        int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
        return Integer.signum(diff);
    } else {
        return Integer.signum(vals1.length - vals2.length);
    }
}