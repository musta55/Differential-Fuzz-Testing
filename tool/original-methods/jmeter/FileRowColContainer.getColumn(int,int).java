/**
 * Get the string for the column from the current row
 *
 * @param row
 *            row number (from 0)
 * @param col
 *            column number (from 0)
 * @return the string (empty if out of bounds)
 * @throws IndexOutOfBoundsException
 *             if the column number is out of bounds
 */
public String getColumn(int row, int col) throws IndexOutOfBoundsException {
    String colData;
    colData = fileData.get(row).get(col);
    log.debug("{}({},{}):{}", fileName, row, col, colData);
    return colData;
}