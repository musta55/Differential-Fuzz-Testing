/**
 * Returns the next row to the caller, and updates it, allowing for wrap
 * round
 *
 * @return the first free (unread) row
 */
public int nextRow() {
    int row = nextRow;
    nextRow++;
    if (nextRow >= fileData.size()) {
        // 0-based
        nextRow = 0;
    }
    log.debug("Row: {}", row);
    return row;
}