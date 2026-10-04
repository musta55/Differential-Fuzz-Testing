@Override
public long write(Sample sample) {
    Validate.validState(writer != null, "No writer set! Call setWriter() first!");
    writer.println(buildRow(sample));
    sampleCount++;
    return sampleCount;
}
// ---- helper method(s) introduced by the refactoring ----
private String buildHeader() {
    StringBuilder row = new StringBuilder();
    for (int i = 0; i < columnCount; i++) {
        row.append(metadata.getColumnName(i));
        if (i < columnCount - 1) {
            row.append(separator);
        }
    }
    return row.toString();
}

private String buildRow(Sample sample) {
    StringBuilder row = new StringBuilder();
    char[] specials = new char[] { separator, CSVSaveService.QUOTING_CHAR, CharUtils.CR, CharUtils.LF };
    for (int i = 0; i < columnCount; i++) {
        String data = sample.getData(i);
        row.append(CSVSaveService.quoteDelimiters(data, specials)).append(separator);
    }
    row.setLength(row.length() - 1);
    return row.toString();
}

