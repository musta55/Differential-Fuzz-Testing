/**
 * Write the csv header. If samples have already been written then a row with
 * header information will be written in the middle of the file.
 */
public void writeHeader() {
    Validate.validState(writer != null, "No writer set! Call setWriter() first!");
    StringBuilder row = new StringBuilder();
    for (int i = 0; i < columnCount; i++) {
        row.append(metadata.getColumnName(i));
        if (i < columnCount - 1) {
            row.append(separator);
        }
    }
    writer.println(row.toString());
}