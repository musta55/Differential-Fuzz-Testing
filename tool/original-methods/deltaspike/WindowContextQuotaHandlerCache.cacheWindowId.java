/**
 * @param currentWindowId window-id which gets processed right now
 * @return true if the previously checked window-id is the same, false otherwise
 */
public boolean cacheWindowId(String currentWindowId) {
    boolean result = currentWindowId.equals(this.checkedWindowId);
    this.checkedWindowId = currentWindowId;
    return result;
}