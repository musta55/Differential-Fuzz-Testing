@Override
public long write(Sample sample) {
    Validate.validState(writer != null, "No writer set! Call setWriter() first!");
    StringBuilder row = new StringBuilder();
    char[] specials = new char[] { separator, CSVSaveService.QUOTING_CHAR, CharUtils.CR, CharUtils.LF };
    for (int i = 0; i < columnCount; i++) {
        String data = sample.getData(i);
        row.append(CSVSaveService.quoteDelimiters(data, specials)).append(separator);
    }
    row.setLength(row.length() - 1);
    writer.println(row.toString());
    sampleCount++;
    return sampleCount;
}