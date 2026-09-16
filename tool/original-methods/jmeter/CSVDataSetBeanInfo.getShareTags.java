/**
 * @return array of String for possible sharing modes
 */
public static String[] getShareTags() {
    String[] copy = new String[SHARE_TAGS.length];
    System.arraycopy(SHARE_TAGS, 0, copy, 0, SHARE_TAGS.length);
    return copy;
}