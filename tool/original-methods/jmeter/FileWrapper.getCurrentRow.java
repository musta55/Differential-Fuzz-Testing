/**
 * Gets the current row number (mainly for error reporting)
 *
 * @param file
 *            name of the file for which the row number is asked
 * @return the current row number for this thread, or <code>-1</code> if
 *         <code>file</code> was not opened yet
 */
public static int getCurrentRow(String file) {
    Map<String, FileWrapper> my = filePacks.get();
    FileWrapper fw = my.get(file);
    if (// Not yet open
    fw == null) {
        return -1;
    }
    return fw.currentRow;
}